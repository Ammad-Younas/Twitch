package com.madiwist.twitch.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.madiwist.twitch.core.domain.models.UserItem
import com.madiwist.twitch.core.presentation.ui.theme.Shapes
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.core.presentation.util.ErrorImageLoading
import com.madiwist.twitch.core.util.Constants

@Composable
fun UserProfileItem(
    modifier: Modifier = Modifier,
    actionIcon: @Composable () -> Unit = {},
    user: UserItem,
    ownUserId: String = "",
    onItemClick: () -> Unit = {},
    onActionItemClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpaceSmall),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(5.dp),
        onClick = onItemClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpaceMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.profilePictureUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.clip(Shapes.extraLarge).size(Constants.PROFILE_PICTURE_SIZE_LARGE - 75.dp),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(), 
                        contentAlignment = Alignment.Center) 
                    {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    BrokenImage(
                        modifier = Modifier.fillMaxSize(),
                        errorImageLoading = ErrorImageLoading.PROFILE_TYPE
                    )
                }
            )
            Spacer(Modifier.width(SpaceMedium))
            Column(
                modifier = Modifier
                    .fillMaxHeight().weight(8f)
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(SpaceSmall))
                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
            if (user.userId != ownUserId) {
                IconButton(
                    onClick = onActionItemClick,
                    modifier = Modifier
                ) {
                    actionIcon()
                }
            }
        }
    }
}