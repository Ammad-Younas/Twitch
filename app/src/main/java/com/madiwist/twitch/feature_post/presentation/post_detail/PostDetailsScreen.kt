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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.models.Comment
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.presentation.components.BrokenImage
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.ui.theme.ExtraSpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.ExtraSpaceSmall
import com.madiwist.twitch.core.presentation.ui.theme.Shapes
import com.madiwist.twitch.core.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.core.presentation.util.ErrorImageLoading
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_post.domain.util.ActionRow

@Composable
fun PostDetailsScreen(
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    post: Post,
) {
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
                .weight(1f)
                .padding(SpaceSmall)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .clip(MaterialTheme.shapes.medium)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                item {
                    Column {
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
                                    modifier = Modifier.
                                    fillMaxWidth().
                                    height(200.dp),
                                    contentAlignment = Alignment.Center)
                                {
                                    CircularProgressIndicator()
                                }
                            },
                            error = {
                                BrokenImage(
                                    modifier = Modifier.fillMaxWidth(),
                                    errorImageLoading = ErrorImageLoading.POST_TYPE
                                )
                            }
                        )
                        Column(
                            modifier = Modifier
                                .padding(SpaceMedium)
                        ) {
                            ActionRow(
                                modifier = Modifier.fillMaxWidth(),
                                username = "MADI",
                                onLikeClick = {  },
                                onCommentClick = { },
                                onShareClick = { },
                                onUsernameClick = {  }
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
                                text = stringResource(R.string.post_liked_by_x_people, post.likeCount ?: 0),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                items(10) {
                    Comment(
                        modifier = Modifier.fillMaxWidth(),
                        comment = Comment(
                            commentId = 1,
                            username = "MADI",
                            profilePictureUrl = "",
                            comment = "Lorem Ipsum is simply dummy text of the printing and typesetting industry, Lorem Ipsum.",
                            timeStamp = System.currentTimeMillis(),
                            likeCount = 11,
                            isLiked = true
                        )
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
                        .fillMaxWidth()
                        .weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    value = "",
                    onValueChange = { },
                    placeholder = {
                        Text(
                            "Enter a comment",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    maxLines = 3
                )
                Spacer(Modifier.width(SpaceMedium))
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun Comment(
    modifier: Modifier = Modifier,
    comment: Comment = Comment(),
    onLikeClick : (Boolean) -> Unit = {},
) {
    Card (
        modifier = modifier
            .padding(SpaceSmall),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(5.dp),

    ) {
        Column (
            modifier = Modifier.fillMaxSize().padding(SpaceLarge)
        ) {
            Row (
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(comment.profilePictureUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.clip(Shapes.extraLarge).size(30.dp),
                        loading = {
                            Box(modifier = Modifier.size(30.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        },
                        error = {
                            BrokenImage(
                                modifier = Modifier.size(30.dp),
                                errorImageLoading = ErrorImageLoading.PROFILE_TYPE
                            )
                        }
                    )
                    Spacer(Modifier.width(SpaceMedium))
                    Text(
                        text = comment.username,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "2 days ago"
                )
            }
            Spacer(Modifier.height(SpaceMedium))
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = comment.comment,
                )
                Spacer(Modifier.width(ExtraSpaceSmall))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    IconButton(
                        onClick = {
                            onLikeClick(comment.isLiked)
                        },
                        modifier = Modifier.size(Constants.ENGAGEMENT_ICON_SIZE)
                    ) {
                        Icon(
                            imageVector = if (comment.isLiked) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Filled.FavoriteBorder
                            },
                            contentDescription = if (comment.isLiked) {
                                stringResource(R.string.unlike)
                            } else {
                                stringResource(R.string.liked)
                            },
                            tint = if (comment.isLiked) {
                                Color.Red
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            }
                        )
                    }
                    Text(
                        text = stringResource(R.string.liked_comments, comment.likeCount),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(SpaceMedium))
            Text(
                text = stringResource(R.string.liked_by, comment.likeCount),
                fontWeight = FontWeight.Bold
            )
        }
    }
}