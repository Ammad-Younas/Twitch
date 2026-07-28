package com.madiwist.twitch.feature_profile.presentation.profile.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.core.util.toPx
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_profile.domain.model.Skill

@Composable
fun BannerSection(
    modifier: Modifier = Modifier,
    leftIconModifier: Modifier = Modifier,
    rightIconModifier: Modifier = Modifier,
    bannerUrl: String? = null,
    topSkillUrls : List<Skill> = emptyList(),
    shouldShowGithub: Boolean = false,
    shouldShowInstagram: Boolean = false,
    shouldShowLinkedIn: Boolean = false,
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
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bannerUrl)
                    .crossfade(true)
                    .build()
            ),
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
            modifier = leftIconModifier
                .height(Constants.PROFILE_ICONS_SIZE)
                .align(Alignment.BottomStart)
                .padding(SpaceSmall)
        ) {
            topSkillUrls.forEach { skillUrl ->
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(skillUrl.imageUrl)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(Constants.PROFILE_ICONS_SIZE)
                )
            }
//            Image(
//                painter = painterResource(R.drawable.js),
//                contentDescription = "JS",
//                modifier = Modifier.size(Constants.PROFILE_ICONS_SIZE)
//            )
//            Image(
//                painter = painterResource(R.drawable.kotlin),
//                contentDescription = "Kotlin",
//                modifier = Modifier.size(Constants.PROFILE_ICONS_SIZE)
//            )
//            Image(
//                painter = painterResource(R.drawable.android),
//                contentDescription = "JS",
//                modifier = Modifier.size(Constants.PROFILE_ICONS_SIZE)
//            )
        }
        Row(
            modifier = rightIconModifier
                .height(Constants.PROFILE_ICONS_SIZE)
                .align(Alignment.BottomEnd)
                .padding(SpaceSmall)
        ) {
            if (shouldShowGithub) {
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
            }
            if (shouldShowInstagram){
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
            }
            if (shouldShowLinkedIn){
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
}








