package com.madiwist.twitch.presentation.main_feed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import com.madiwist.twitch.presentation.utils.Screen

@Composable
fun MainFeedScreen (
    navController: NavController
) {
    Scaffold(
        topBar = {
            TwitchToolBar(
                navController = navController,
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(stringResource(R.string.your_feed))
                },
                showBackArrow = true,
                navActions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.SearchScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = stringResource(R.string.Search)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Post(
                    modifier = Modifier.padding(SpaceMedium),
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
















