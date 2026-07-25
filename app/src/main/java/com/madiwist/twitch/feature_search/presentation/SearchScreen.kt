package com.madiwist.twitch.feature_search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.models.User
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.presentation.components.TwitchTextField
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.components.UserProfileItem
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TwitchToolBar(
            navController = navController,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(stringResource(R.string.search_for_users))
            },
            showBackArrow = true,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceMedium)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
        ) {
            TwitchTextField(
                text = viewModel.searchState.value.text,
                onValueChange = {
                    viewModel.setSearchState(state = TwitchTextFieldState(text = it))
                },
                hint = stringResource(R.string.search),
                error = "",
                leadingIcon = Icons.Filled.Search
            )
            Spacer(Modifier.height(SpaceLarge))
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                items(10) {
                    UserProfileItem(
                        user = User(
                            userId = "6a5fd504ce7b4289845b4f12",
                            username = "Ammad",
                            description = "This is test bio",
                            profilePictureUrl = "http://10.0.2.2:8001/posts/79741234.png",
                            postCount = 330,
                            followerCount = 12,
                            followingCountL = 7
                        ),
                        actionIcon = {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null
                            )
                        },
                        onItemClick = { navController.navigate(Screen.ProfileScreen.route + "?userId=6a5fd504ce7b4289845b4f12") }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}