package com.madiwist.twitch.feature_profile.presentation.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.models.User
import com.madiwist.twitch.core.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.core.util.Constants

@Composable
fun ProfileHeaderSection(
    user: User,
    modifier: Modifier = Modifier,
    isOwnProfile: Boolean = true,
    onEditClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            user = user,
            onFollowCLick = {},
            isOwnProfile = isOwnProfile
        )
    }
}






















