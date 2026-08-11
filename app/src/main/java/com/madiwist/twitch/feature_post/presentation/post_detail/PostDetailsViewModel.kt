package com.madiwist.twitch.feature_post.presentation.post_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.ParentType
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_auth.domain.use_case.AuthenticateUseCase
import com.madiwist.twitch.feature_post.domain.use_case.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val authenticateUseCase: AuthenticateUseCase
) : ViewModel() {

    private val _postDetailsState = mutableStateOf(PostDetailsState())
    val postDetailsState: State<PostDetailsState> = _postDetailsState

    private val _commentFieldState = mutableStateOf(TwitchTextFieldState())
    val commentFieldState: State<TwitchTextFieldState> = _commentFieldState

    private val _commentState = mutableStateOf(CommentState())
    val commentState: State<CommentState> = _commentState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val postModifications = postUseCases.getPostModificationsUseCase()
    val commentModifications = postUseCases.getCommentModificationsUseCase()

    private var isUserLoggedIn: Boolean = false

    init {
        savedStateHandle.get<String>("postId")?.let { postId ->
            loadPostDetails(postId)
            loadCommentsForPost(postId)
        }
    }


    fun onEvent(event: PostDetailsEvent) {
        when (event) {
            is PostDetailsEvent.LikePost -> {
                val post = event.post
                val isLiked = post.isLiked == true
                toggleLikeForParent(
                    parentId = post.id ?: return,
                    parentType = ParentType.Post.type,
                    isLiked = isLiked
                )

            }

            is PostDetailsEvent.Comment -> {
                createComment(
                    postId = savedStateHandle.get<String>("postId") ?: "",
                    comment = commentFieldState.value.text
                )
            }

            is PostDetailsEvent.EnteredComment -> {
                _commentFieldState.value = commentFieldState.value.copy(
                    text = event.comment
                )
            }

            is PostDetailsEvent.LikeComment -> {
                val comment =
                    postDetailsState.value.comments.find { it.commentId == event.commentId }
                val currentComment =
                    commentModifications.value[event.commentId] ?: comment ?: return
                val isLiked = currentComment.isLiked
                toggleLikeForParent(
                    parentId = event.commentId,
                    parentType = ParentType.Comment.type,
                    isLiked = isLiked
                )
            }
        }
    }

    private fun loadPostDetails(postId: String) {
        viewModelScope.launch {
            _postDetailsState.value = postDetailsState.value.copy(isLoadingPost = true)
            when (val result = postUseCases.getPostDetailsUseCase(postId)) {
                is Resource.Success -> {
                    _postDetailsState.value = postDetailsState.value.copy(
                        post = result.data,
                        isLoadingPost = false
                    )
                }

                is Resource.Error -> {
                    _postDetailsState.value = postDetailsState.value.copy(
                        isLoadingPost = false
                    )
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }

    private fun loadCommentsForPost(postId: String) {
        viewModelScope.launch {
            _postDetailsState.value = postDetailsState.value.copy(isLoadingComments = true)
            when (val result = postUseCases.getCommentsForPostUseCase(postId)) {
                is Resource.Success -> {
                    _postDetailsState.value = postDetailsState.value.copy(
                        comments = result.data ?: emptyList(),
                        isLoadingComments = false
                    )
                }

                is Resource.Error -> {
                    _postDetailsState.value = postDetailsState.value.copy(
                        isLoadingComments = false
                    )
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }

    private fun createComment(postId: String, comment: String) {
        viewModelScope.launch {
            isUserLoggedIn = authenticateUseCase() is Resource.Success
            if (!isUserLoggedIn){
                _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_user_not_logged_in)))
                return@launch
            }
            _commentState.value = commentState.value.copy(
                isLoading = true
            )
            val result = postUseCases.createCommentUseCase(
                postId = postId,
                comment = comment
            )
            when (result) {
                is Resource.Success -> {
                    _commentFieldState.value = commentFieldState.value.copy(
                        text = ""
                    )
                    _commentState.value = commentState.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            uiText = UiText.StringResource(R.string.comment_posted)
                        )
                    )
                    loadCommentsForPost(postId)
                }

                is Resource.Error -> {
                    _commentState.value = commentState.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }

    private fun toggleLikeForParent(parentId: String, parentType: Int, isLiked: Boolean) {
        viewModelScope.launch {
            isUserLoggedIn = authenticateUseCase() is Resource.Success
            if (!isUserLoggedIn){
                _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_user_not_logged_in)))
                return@launch
            }
            when (parentType) {
                ParentType.Post.type -> {
                    val post = postDetailsState.value.post ?: return@launch
                    val updatedPost = post.copy(
                        isLiked = !isLiked,
                        likeCount = if (isLiked) {
                            (post.likeCount ?: 0) - 1
                        } else {
                            (post.likeCount ?: 0) + 1
                        }
                    )
                    _postDetailsState.value = postDetailsState.value.copy(
                        post = updatedPost
                    )
                    postUseCases.toggleLikeStateForParentUseCase.updatePostModification(
                        parentId,
                        updatedPost
                    )
                }

                ParentType.Comment.type -> {
                    val comment = postDetailsState.value.comments.find { it.commentId == parentId }
                    val currentComment =
                        commentModifications.value[parentId] ?: comment ?: return@launch
                    val updatedComment = currentComment.copy(
                        isLiked = !isLiked,
                        likeCount = if (isLiked) currentComment.likeCount - 1 else currentComment.likeCount + 1
                    )
                    _postDetailsState.value = postDetailsState.value.copy(
                        comments = postDetailsState.value.comments.map {
                            if (it.commentId == parentId) {
                                updatedComment
                            } else it
                        }
                    )
                    postUseCases.toggleLikeStateForParentUseCase.updateCommentModification(
                        parentId,
                        updatedComment
                    )
                }
            }
            val result = postUseCases.toggleLikeStateForParentUseCase(
                parentId = parentId,
                parentType = parentType,
                isLiked = isLiked
            )
            when (result) {
                is Resource.Success -> Unit
                is Resource.Error -> {
                    when (parentType) {
                        ParentType.Post.type -> {
                            val post = postDetailsState.value.post ?: return@launch
                            val revertedPost = post.copy(
                                isLiked = isLiked,
                                likeCount = if (isLiked) {
                                    (post.likeCount ?: 0) + 1
                                } else {
                                    (post.likeCount ?: 0) - 1
                                }
                            )
                            _postDetailsState.value = postDetailsState.value.copy(
                                post = revertedPost
                            )
                            postUseCases.toggleLikeStateForParentUseCase.abortPostModification(
                                parentId
                            )
                        }

                        ParentType.Comment.type -> {
                            val comment =
                                postDetailsState.value.comments.find { it.commentId == parentId }
                            val currentComment =
                                commentModifications.value[parentId] ?: comment ?: return@launch
                            val revertedComment = currentComment.copy(
                                isLiked = isLiked,
                                likeCount = if (isLiked) currentComment.likeCount + 1 else currentComment.likeCount - 1
                            )
                            _postDetailsState.value = postDetailsState.value.copy(
                                comments = postDetailsState.value.comments.map {
                                    if (it.commentId == parentId) {
                                        revertedComment
                                    } else it
                                }
                            )
                            postUseCases.toggleLikeStateForParentUseCase.abortCommentModification(
                                parentId
                            )
                        }
                    }
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }
}