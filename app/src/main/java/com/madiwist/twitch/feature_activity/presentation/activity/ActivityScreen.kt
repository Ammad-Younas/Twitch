package com.madiwist.twitch.feature_activity.presentation.activity

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.models.Activity
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.util.DateFormatUtil
import com.madiwist.twitch.feature_activity.domain.util.ActivityAction
import com.madiwist.twitch.feature_activity.presentation.activity.components.ActivityItem
import kotlin.random.Random

@Composable
fun ActivityScreen(
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: ActivityViewModel = hiltViewModel()
) {
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
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(SpaceMedium)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .clip(MaterialTheme.shapes.medium)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                items(10) {
                    ActivityItem(
                        activity = Activity(
                            username = "MADI",
                            actionType = if (Random.nextInt(2) == 0) {
                                ActivityAction.LikedPost
                            } else {
                                ActivityAction.CommentedOnPost
                            },
                            formatedTime = DateFormatUtil.timestampToFormatedString(
                                timestamp = System.currentTimeMillis(),
                                pattern = "MMM dd, HH:mm"
                            )
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }

        }
    }
}