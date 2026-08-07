package com.madiwist.twitch.feature_post.presentation.post_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.components.BrokenImage
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.ui.theme.ExtraSpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.core.presentation.util.ErrorImageLoading
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.presentation.util.asString
import com.madiwist.twitch.feature_post.domain.util.ActionRow
import com.madiwist.twitch.feature_post.presentation.post_detail.components.CommentItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PostDetailsScreen(
    onNavigateUp: () -> Unit = {},
    snackbarHostState: SnackbarHostState,
    viewModel: PostDetailsViewModel = hiltViewModel()
) {
    val postDetailsState = viewModel.postDetailsState.value
    val commentFieldState = viewModel.commentFieldState.value

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TwitchToolBar(
            onNavigateUp = onNavigateUp,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(stringResource(R.string.your_feed))
            },
            showBackArrow = true,
        )
        Column(
            modifier = Modifier
                .padding(SpaceSmall)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .clip(MaterialTheme.shapes.medium)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val post = postDetailsState.post
                        if (post != null) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(post.imageUrl?.replace("127.0.0.1", "10.0.2.2"))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.post_image),
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth,
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                },
                                error = {
                                    BrokenImage(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        errorImageLoading = ErrorImageLoading.POST_TYPE
                                    )
                                }
                            )
                            Column(
                                modifier = Modifier.padding(SpaceMedium)
                            ) {
                                ActionRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    username = postDetailsState.post.username ?: "",
                                    onUsernameClick = { },
                                    onLikeClick = { viewModel.onEvent(PostDetailsEvent.LikePost) },
                                    onCommentClick = { },
                                    onShareClick = { },
                                    isLiked = postDetailsState.post.isLiked == true
                                )
                                Spacer(modifier = Modifier.height(SpaceMedium))
                                Text(
                                    text = post.description ?: "",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                                Spacer(modifier = Modifier.height(ExtraSpaceLarge))
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(
                                        R.string.post_liked_by_x_people,
                                        post.likeCount ?: 0
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else if (postDetailsState.isLoadingPost) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
                if (postDetailsState.isLoadingComments) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                items(
                    items = postDetailsState.comments,
                    key = { it.commentId }
                ) { comment ->
                    CommentItem(
                        comment = comment,
                        onLikeClick = { viewModel.onEvent(PostDetailsEvent.LikeComment(comment.commentId)) }
                    )
                }
            }
            Spacer(Modifier.height(SpaceMedium))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = SpaceMedium,
                        end = SpaceMedium,
                        top = SpaceSmall,
                        bottom = SpaceLarge
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .weight(1f),
                    value = commentFieldState.text,
                    onValueChange = {
                        viewModel.onEvent(PostDetailsEvent.EnteredComment(it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_a_comment),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    maxLines = 3,
                    shape = RoundedCornerShape(28.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Send
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(Modifier.width(SpaceMedium))
                if (viewModel.commentState.value.isLoading){
                    CircularProgressIndicator()
                } else {
                    FilledIconButton(
                        onClick = {
                            viewModel.onEvent(PostDetailsEvent.Comment)
                        },
                        enabled = commentFieldState.text.isNotBlank(),
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = stringResource(R.string.send),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}