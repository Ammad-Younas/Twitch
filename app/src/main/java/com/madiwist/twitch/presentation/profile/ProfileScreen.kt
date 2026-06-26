package com.madiwist.twitch.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.domain.models.Post
import com.madiwist.twitch.domain.models.User
import com.madiwist.twitch.presentation.components.Post
import com.madiwist.twitch.presentation.components.TwitchToolBar
import com.madiwist.twitch.presentation.profile.components.BannerSection
import com.madiwist.twitch.presentation.profile.components.ProfileHeaderSection
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium

@Composable
fun ProfileScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TwitchToolBar(
            navController = navController,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(stringResource(R.string.your_profile))
            },
            showBackArrow = false,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .clip(MaterialTheme.shapes.medium)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                item {
                    BannerSection (
                        modifier = Modifier.aspectRatio(2.5f)
                    )
                }
                item {
                    ProfileHeaderSection(
                        user = User(
                            username = "MADI",
                            description = "BoxWithConstraints scope is not used, BoxWithConstraints scope is not usedBoxWithConstraints scope is not used, BoxWithConstraints scope is not used",
                            profilePictureUrl = "",
                            postCount = 35,
                            followerCount = 353,
                            followingCountL = 435
                        )
                    )
                }
                items(20){
                    Column(modifier = Modifier.fillMaxSize().padding(SpaceMedium)) {
                        Post(
                            post = Post(
                                username = "MADI",
                                imageUrl = "",
                                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since 1966, when designers",
                                likeCount = 23,
                                commentCount = 15
                            ),
                            onPostClick = {  }
                        )
                    }
                }
            }

        }
    }
}










