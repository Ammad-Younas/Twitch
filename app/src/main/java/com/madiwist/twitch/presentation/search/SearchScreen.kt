package com.madiwist.twitch.presentation.search

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
import com.madiwist.twitch.domain.models.User
import com.madiwist.twitch.presentation.components.TwitchTextField
import com.madiwist.twitch.presentation.components.TwitchToolBar
import com.madiwist.twitch.presentation.components.UserProfileItem
import com.madiwist.twitch.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.presentation.utils.states.TwitchTextFieldState

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
                error = viewModel.searchState.value.error,
                leadingIcon = Icons.Filled.Search
            )
            Spacer(Modifier.height(SpaceLarge))
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                items(10) {
                    UserProfileItem(
                        user = User(
                            username = "MADI",
                            description = "BoxWithConstraints scope is not used, BoxWithConstraints scope is not usedBoxWithConstraints scope is not used, BoxWithConstraints scope is not used",
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