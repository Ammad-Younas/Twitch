package com.madiwist.twitch.presentation.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.madiwist.twitch.R
import com.madiwist.twitch.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.presentation.utils.toPx
import com.madiwist.twitch.utils.Constants

@Composable
fun BannerSection(
    modifier: Modifier = Modifier,
    onGitHubClick: () -> Unit = {},
    onInstagramClick: () -> Unit = {},
    onLinkedInClick: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier.padding(bottom = SpaceSmall)
    ) {
        Image(
            modifier = modifier
                .fillMaxSize(),
            painter = painterResource(R.drawable.profile_banner),
            contentDescription = stringResource(R.string.banner_image),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier.fillMaxSize().background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black
                    ),
                    startY = constraints.maxHeight - Constants.PROFILE_ICONS_SIZE.toPx() * 2f,
                )
            ),
        )
        Row(
            modifier = Modifier
                .height(Constants.PROFILE_ICONS_SIZE)
                .align(Alignment.BottomStart)
                .padding(SpaceSmall)
        ) {
            Image(
                painter = painterResource(R.drawable.js),
                contentDescription = "JS",
                modifier = Modifier.size(Constants.PROFILE_ICONS_SIZE)
            )
            Image(
                painter = painterResource(R.drawable.kotlin),
                contentDescription = "Kotlin",
                modifier = Modifier.size(Constants.PROFILE_ICONS_SIZE)
            )
            Image(
                painter = painterResource(R.drawable.android),
                contentDescription = "JS",
                modifier = Modifier.size(Constants.PROFILE_ICONS_SIZE)
            )
        }
        Row(
            modifier = Modifier
                .height(Constants.PROFILE_ICONS_SIZE)
                .align(Alignment.BottomEnd)
                .padding(SpaceSmall)
        ) {
            IconButton (
                onClick = onGitHubClick ,
                modifier = Modifier
                    .height(Constants.PROFILE_ICONS_SIZE)
                    .size(Constants.PROFILE_ICONS_SIZE)
            ) {
                Image(
                    painter = painterResource(R.drawable.github),
                    contentDescription = "Github",
                )
            }
            IconButton (
                onClick = onInstagramClick,
                modifier = Modifier
                    .height(Constants.PROFILE_ICONS_SIZE)
                    .size(Constants.PROFILE_ICONS_SIZE)
            ) {
                Image(
                    painter = painterResource(R.drawable.instagram),
                    contentDescription = "Instagram",
                )
            }
            IconButton (
                onClick = onLinkedInClick,
                modifier = Modifier
                    .height(Constants.PROFILE_ICONS_SIZE)
                    .size(Constants.PROFILE_ICONS_SIZE)
            ) {
                Image(
                    painter = painterResource(R.drawable.linkedin),
                    contentDescription = "LinkedIn",
                )
            }
        }
    }
}








