package com.madiwist.twitch.presentation.main_feed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.madiwist.twitch.domain.models.Post as PostModel
import com.madiwist.twitch.presentation.components.Post
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium

@Composable
fun MainFeedScreen (
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = SpaceMedium)
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
