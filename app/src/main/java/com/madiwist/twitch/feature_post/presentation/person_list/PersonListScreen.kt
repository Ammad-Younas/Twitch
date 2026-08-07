package com.madiwist.twitch.feature_post.presentation.person_list

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.components.UserProfileItem
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.presentation.util.asString
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PersonListScreen(
    onNavigate: (String) -> Unit = {},
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit = {},
    viewModel: PersonListViewModel = hiltViewModel()
) {

    val usersState = viewModel.usersState.value
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
                else -> Unit
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TwitchToolBar(
            onNavigateUp = onNavigateUp,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(stringResource(R.string.liked_by_toolbar))
            },
            showBackArrow = true,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(SpaceMedium)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {
                    items(usersState.users) { user ->
                        UserProfileItem(
                            user = user,
                            actionIcon = {
                                IconButton(
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        imageVector = if (user.isFollowing){
                                            Icons.Default.PersonRemove
                                        } else {
                                            Icons.Default.PersonAdd
                                        },
                                        contentDescription = null,
                                    )
                                }
                            },
                            onItemClick = { onNavigate(Screen.ProfileScreen.route + "?userId=${user.userId}") },
                            onActionItemClick = { viewModel.onEvent(PersonListEvent.ToggleFollowState(user.userId)) }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            if (usersState.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}