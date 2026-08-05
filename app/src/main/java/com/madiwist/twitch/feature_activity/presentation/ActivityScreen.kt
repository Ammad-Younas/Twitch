package com.madiwist.twitch.feature_activity.presentation

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.feature_activity.presentation.components.ActivityItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ActivityScreen(
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: ActivityViewModel = hiltViewModel()
) {

    val activityState = viewModel.activityState.value
    val activities = viewModel.activities.collectAsLazyPagingItems()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event.route)
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
                Text(stringResource(R.string.your_activity))
            },
            showBackArrow = false,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .padding(SpaceMedium)
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                    .clip(MaterialTheme.shapes.medium)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {
                    items(activities.itemCount) { index ->
                        val activity = activities[index]
                        activity?.let {
                            ActivityItem(
                                activity = it,
                                onUserClick = { userId ->
                                    viewModel.onEvent(ActivityEvent.ClickedOnUser(userId)) 
                                },
                                onParentClick = { parentId -> 
                                    viewModel.onEvent(ActivityEvent.ClickedOnParent(parentId)) 
                                }
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            if (activityState.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}