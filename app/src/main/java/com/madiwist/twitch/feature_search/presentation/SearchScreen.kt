package com.madiwist.twitch.feature_search.presentation

import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.components.TwitchTextField
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.components.UserProfileItem
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium

@Composable
fun SearchScreen(
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {

    val searchState = viewModel.searchState.value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TwitchToolBar(
            onNavigateUp = onNavigateUp,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(stringResource(R.string.search_for_users))
            },
            showBackArrow = true,
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpaceMedium)
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                ) {
                    TwitchTextField(
                        text = viewModel.searchFieldState.value.text,
                        onValueChange = {
                            viewModel.onEvent(SearchEvent.Query(it))
                        },
                        hint = stringResource(R.string.search),
                        leadingIcon = Icons.Filled.Search
                    )
                    Spacer(Modifier.height(SpaceLarge))
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                    ) {
                        items(searchState.userItems) { user ->
                            UserProfileItem(
                                user = user,
                                ownUserId = searchState.ownUserId,
                                actionIcon = {
                                    Icon(
                                        imageVector = if (user.isFollowing) {
                                            Icons.Default.PersonRemove
                                        } else {
                                            Icons.Default.PersonAdd
                                        },
                                        contentDescription = null,
                                    )
                                },
                                onActionItemClick = {
                                    viewModel.onEvent(SearchEvent.ToggleFollowState(user.userId))
                                },
                                onItemClick = { onNavigate(Screen.ProfileScreen.route + "?userId=${user.userId}") }
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
            if (searchState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}