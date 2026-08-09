package com.madiwist.twitch.feature_profile.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madiwist.twitch.core.domain.models.User
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.presentation.util.asString
import com.madiwist.twitch.feature_post.domain.util.PostItem
import com.madiwist.twitch.feature_profile.presentation.profile.components.BannerSection
import com.madiwist.twitch.feature_profile.presentation.profile.components.ProfileHeaderSection
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileScreen(
    userId: String? = null,
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    snackbarHostState: SnackbarHostState,
    profilePictureSize: Dp = 80.dp,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val state = viewModel.profileState.value
    val context = LocalContext.current
    val postModifications by viewModel.postModifications.collectAsState()

    val toolbarHeightCollapsed = 75.dp
    val bannerHeight = (LocalConfiguration.current.screenWidthDp / 2.5f).dp
    val toolbarHeightExpanded = bannerHeight + profilePictureSize

    val maxOffset = with(androidx.compose.ui.platform.LocalDensity.current) {
        toolbarHeightExpanded.toPx() - toolbarHeightCollapsed.toPx()
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = viewModel.toolBarOffsetY.value + delta
                viewModel.setToolbarOffsetY(
                    newOffset.coerceIn(-maxOffset, 0f)
                )
                viewModel.setExpandedRatio(
                    ((maxOffset + viewModel.toolBarOffsetY.value) / maxOffset).coerceIn(0f, 1f)
                )
                return Offset.Zero
            }
        }
    }

    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = true) {
        userId?.let {
            viewModel.getProfile(it)
        }
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }
                is UiEvent.Refresh -> {
                   // viewModel.getProfile(userId ?: "")
                }
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = lazyListState
        ) {
            item {
                Spacer(
                    modifier = Modifier.height(
                        toolbarHeightExpanded - (profilePictureSize / 2f)
                    )
                )
            }
            item {
                state.profile?.let { profile ->
                    ProfileHeaderSection(
                        user = User(
                            userId = profile.userId,
                            profilePictureUrl = profile.profilePictureUrl,
                            username = profile.username,
                            description = profile.bio,
                            followerCount = profile.followerCount,
                            followingCount = profile.followingCount,
                            postCount = profile.postCount
                        ),
                        isOwnProfile = profile.isOwnProfile,
                        isFollowing = profile.isFollowing
                    )
                }
            }
            itemsIndexed(state.posts) { index, post ->
                if (index >= state.posts.size - 1 && !state.endReached && !state.isLoadingPosts) {
                    viewModel.loadNextPosts()
                }
                Spacer(modifier = Modifier.height(SpaceMedium))
                PostItem(
                    post = postModifications[post.id] ?: post,
                    onPostClick = {
                        onNavigate(Screen.PostDetailsScreen.route + "/${post.id}")
                    },
                    onLikeClick = {
                        viewModel.onEvent(ProfileEvent.LikePost(post))
                    },
                    onCommentClick = {
                        onNavigate(Screen.PostDetailsScreen.route + "/${post.id}?shouldShowKeyboard=true")
                    },
                    onShareClick = {
                        // viewModel.onEvent(ProfileEvent.SharePost(post))
                    },
                    onUsernameClick = {
                        // If we are already on the user's profile, maybe do nothing
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(SpaceLarge))
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            state.profile?.let { profile ->
                BannerSection(
                    modifier = Modifier
                        .height(bannerHeight * viewModel.expandedRatio.value),
                    bannerUrl = profile.bannerUrl,
                    topSkillUrls = profile.topSkillUrls,
                    shouldShowGithub = !profile.gitHubUrl.isNullOrBlank(),
                    shouldShowInstagram = !profile.instagramUrl.isNullOrBlank(),
                    shouldShowLinkedIn = !profile.linkedInUrl.isNullOrBlank()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SpaceLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(profilePictureSize * viewModel.expandedRatio.value)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        // Profile Image here
                    }
                    Spacer(modifier = Modifier.width(SpaceSmall))
                    Column {
                        Text(
                            text = profile.username,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
