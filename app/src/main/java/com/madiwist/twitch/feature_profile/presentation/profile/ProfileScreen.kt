package com.madiwist.twitch.feature_profile.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.domain.models.User
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.presentation.util.asString
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.toPx
import com.madiwist.twitch.feature_post.domain.util.Post
import com.madiwist.twitch.feature_profile.presentation.profile.components.BannerSection
import com.madiwist.twitch.feature_profile.presentation.profile.components.ProfileHeaderSection
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    snackbarHostState: SnackbarHostState,
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val toolBarHeightCollapsed = 100.dp
    val lazyListState = rememberLazyListState()

    val expandedRatio by viewModel.expandedRatio

    val containerWidth = LocalWindowInfo.current.containerSize.width
    val bannerHeight = with(LocalDensity.current) { (containerWidth.toDp() / 2.5f) }
    val toolBarHeightExpanded = remember(bannerHeight) { bannerHeight + Constants.PROFILE_PICTURE_SIZE_LARGE }

    val maxOffset = remember(toolBarHeightExpanded, toolBarHeightCollapsed) { toolBarHeightExpanded - toolBarHeightCollapsed }

    val imageCollapsedOffsetY = remember(toolBarHeightCollapsed) { (toolBarHeightCollapsed - Constants.PROFILE_PICTURE_SIZE_LARGE / 2f) / 2f }

    val iconSizeExpanded = Constants.PROFILE_ICONS_SIZE
    val iconCollapsedOffsetY = remember(toolBarHeightCollapsed, iconSizeExpanded) { (toolBarHeightCollapsed - iconSizeExpanded) / 2f }

    val iconHorizontalCenterLength = remember(containerWidth) {
        containerWidth / 4f - (Constants.PROFILE_PICTURE_SIZE_LARGE / 2f).toPx() - SpaceSmall.toPx()
    }

    val nestedScrollConnection = remember(maxOffset) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val currentOffset = viewModel.toolBarOffsetY.value
                if (delta < 0f) {
                    val newOffset = (currentOffset + delta).coerceIn(-maxOffset.toPx(), 0f)
                    viewModel.setToolbarOffsetY(newOffset)
                    viewModel.setExpandedRatio((newOffset + maxOffset.toPx()) / maxOffset.toPx())
                } else if (delta > 0f && lazyListState.firstVisibleItemIndex == 0) {
                    val newOffset = (currentOffset + delta).coerceIn(-maxOffset.toPx(), 0f)
                    viewModel.setToolbarOffsetY(newOffset)
                    viewModel.setExpandedRatio((newOffset + maxOffset.toPx()) / maxOffset.toPx())
                }
                return Offset.Zero
            }
        }
    }

    val profileState = viewModel.profileState.value
    val context = LocalContext.current


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.SnackbarEvent -> {
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
                Text(stringResource(R.string.your_profile))
            },
            showBackArrow = false,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .clip(MaterialTheme.shapes.medium)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().nestedScroll(nestedScrollConnection)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState
                ) {
                    item {
                        Spacer(Modifier.height((toolBarHeightExpanded) - Constants.PROFILE_PICTURE_SIZE_LARGE / 2f))
                    }
                    item {
                        profileState.profile?.let { profile ->
                            ProfileHeaderSection(
                                user = User(
                                    userId = profile.userId,
                                    username = profile.username,
                                    description = profile.bio,
                                    profilePictureUrl = profile.profilePictureUrl,
                                    postCount = profile.postCount,
                                    followerCount = profile.followerCount,
                                    followingCount = profile.followingCount
                                ),
                                isOwnProfile = profile.isOwnProfile,
                                onEditClick = { onNavigate(Screen.EditProfileScreen.route) }
                            )
                        }
                    }
                    item {
                        Spacer(Modifier.height(SpaceLarge))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.all_posts),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(SpaceLarge))
                    }
                    items(20){

                        Column(modifier = Modifier.fillMaxSize().padding(SpaceMedium)) {
                            Post(
                                post = Post(
                                    username = "MADI",
                                    imageUrl = "",
                                    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since 1966, when designers",
                                    likeCount = 23,
                                    commentCount = 15,
                                    timestamp = System.currentTimeMillis()
                                ),
                                onPostClick = { onNavigate(Screen.PostDetailsScreen.route) }
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                ) {
                    profileState.profile?.let { profile ->
                        BannerSection(
                            modifier = Modifier
                                .height(
                                    (bannerHeight * expandedRatio).coerceIn(
                                        minimumValue = toolBarHeightCollapsed,
                                        maximumValue = bannerHeight
                                    )
                                ),
                            leftIconModifier = Modifier.graphicsLayer {
                                translationY = (1f - expandedRatio) * (-iconCollapsedOffsetY.toPx())
                                translationX = (1 - expandedRatio) * iconHorizontalCenterLength

                            },
                            rightIconModifier = Modifier.graphicsLayer {
                                translationY = (1f - expandedRatio) * (-iconCollapsedOffsetY.toPx())
                                translationX = (1 - expandedRatio) * (-iconHorizontalCenterLength)

                            },
                            topSkillUrls = profile.topSkillUrls,
                            shouldShowGithub = profile.gitHubUrl != null,
                            shouldShowInstagram = profile.instagramUrl != null,
                            shouldShowLinkedIn = profile.linkedInUrl != null,
                            bannerUrl = profile.bannerUrl
                        )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(profile.profilePictureUrl)
                                    .crossfade(true)
                                    .build()
                            ),
                            contentDescription = stringResource(R.string.profile_image),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .graphicsLayer {
                                    translationY = -(Constants.PROFILE_PICTURE_SIZE_LARGE.toPx() / 2f) - (1f - expandedRatio) * imageCollapsedOffsetY.toPx()
                                    transformOrigin = TransformOrigin(
                                        pivotFractionX = 0.5f,
                                        pivotFractionY = 0f
                                    )
                                    val scale = 0.5f + expandedRatio * 0.5f
                                    scaleX = scale
                                    scaleY = scale
                                }
                                .size(Constants.PROFILE_PICTURE_SIZE_LARGE)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = Color.White,
                                    shape = CircleShape
                                ),
                        )
                    }
                }
            }
        }
    }
}










