package com.madiwist.twitch.presentation.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.madiwist.twitch.R
import com.madiwist.twitch.domain.models.User
import com.madiwist.twitch.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.utils.Constants

@Composable
fun ProfileHeaderSection(
    user: User,
    modifier: Modifier = Modifier,
    isOwnProfile: Boolean = true,
    onEditClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .offset(y = -(Constants.PROFILE_PICTURE_SIZE_LARGE / 2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.profile_image),
            contentDescription = stringResource(R.string.profile_image),
            modifier = Modifier
                .size(Constants.PROFILE_PICTURE_SIZE_LARGE)
                .aspectRatio(1f)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape

                ),
        )
        Spacer(Modifier.height(SpaceSmall))
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.offset(
                x = if (isOwnProfile) SpaceSmall + 15.dp else 0.dp
            )
        ) {
            Text(
                text = user.username ?: "",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
            if (isOwnProfile){
                Spacer(Modifier.width(SpaceMedium))
                IconButton (
                    onClick = onEditClick,
                    modifier = Modifier.size(Constants.ENGAGEMENT_ICON_SIZE)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.edit)
                    )
                }
            }
        }
        Spacer(Modifier.height(SpaceSmall))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceMedium),
            text = user.description ?: "",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(SpaceLarge))
        ProfileStats(
            user = User(
                username = "MADI",
                description = "BoxWithConstraints scope is not used, BoxWithConstraints scope is not usedBoxWithConstraints scope is not used, BoxWithConstraints scope is not used",
                profilePictureUrl = "",
                postCount = 35,
                followerCount = 353,
                followingCountL = 435
            ),
            onFollowCLick = {},
            isOwnProfile = isOwnProfile
        )
    }
}






















