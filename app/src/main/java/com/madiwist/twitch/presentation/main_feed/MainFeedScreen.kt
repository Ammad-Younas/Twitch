package com.madiwist.twitch.presentation.main_feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.domain.models.Post as PostModel
import com.madiwist.twitch.presentation.components.Post
import com.madiwist.twitch.presentation.components.TwitchToolBar
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium

@Composable
fun MainFeedScreen (
    navController: NavController
) {
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TwitchToolBar(
            navController = navController,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text("You Feed")
            },
            showBackArrow = true,
            navActions = {
                IconButton(
                    onClick = {  }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                        contentDescription = stringResource(R.string.Back)
                    )
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .padding(SpaceMedium)
        ) {
            item {
                Post(
                    navController = navController,
                    post = PostModel(
                        username = "MADI",
                        imageUrl = "",
                        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since 1966, when designers",
                        likeCount = 23,
                        commentCount = 15
                    )
                )
            }
        }
    }
}
