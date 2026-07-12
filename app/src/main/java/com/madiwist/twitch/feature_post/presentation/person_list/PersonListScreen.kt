package com.madiwist.twitch.feature_post.presentation.person_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.feature_profile.domain.models.User
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.components.UserProfileItem
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium

@Composable
fun PersonListScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TwitchToolBar(
            navController = navController,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(stringResource(R.string.liked_by))
            },
            showBackArrow = true,
        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(SpaceMedium)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                items(10) {
                    UserProfileItem(
                        user = User(
                            username = "MADI",
                            description = "BoxWithConstraints scope is not used, com.madiwist.twitch BoxWithConstraints scope is not usedBoxWithConstraints scope is not used, BoxWithConstraints scope is not used",
                            profilePictureUrl = "",
                            postCount = 35,
                            followerCount = 353,
                            followingCountL = 435
                        ),
                        actionIcon = {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null
                            )
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}