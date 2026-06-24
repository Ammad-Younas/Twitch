package com.madiwist.twitch.presentation.post_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.domain.models.Post
import com.madiwist.twitch.presentation.components.TwitchScaffold
import com.madiwist.twitch.presentation.components.TwitchToolBar
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.presentation.components.Post

@Composable
fun PostDetailsScreen(
    navController: NavController
) {
    TwitchScaffold(
        topBar = {
            TwitchToolBar(
                navController = navController,
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(stringResource(R.string.your_feed))
                },
                showBackArrow = true,
            )
        },
        navController = navController,
        showBottomBarAndFab = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpaceMedium)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
        ) {
            Post(
                post = Post(
                    username = "MADI",
                    imageUrl = "",
                    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since 1966, when designers",
                    likeCount = 23,
                    commentCount = 15
                ),
                navController = navController,
                modifier = Modifier
            )
        }
    }
}