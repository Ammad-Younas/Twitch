package com.madiwist.twitch.feature_activity.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.models.Activity
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.feature_activity.domain.util.ActivityType

@Composable
fun ActivityItem (
    modifier: Modifier = Modifier,
    activity: Activity
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val actionText = when (activity.activityType) {
                is ActivityType.LikedPost -> stringResource(R.string.liked_post)
                is ActivityType.CommentedOnPost -> stringResource(R.string.commented_on_post)
                is ActivityType.FollowedUser -> stringResource(R.string.followed_you)
                is ActivityType.LikedComment -> stringResource(R.string.liked_comment)
            }

            val targetText = when (activity.activityType) {
                is ActivityType.LikedPost -> stringResource(R.string.your_post)
                is ActivityType.CommentedOnPost -> stringResource(R.string.your_post)
                is ActivityType.LikedComment -> stringResource(R.string.your_comment)
                is ActivityType.FollowedUser -> ""
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(activity.username)
                    }

                    append(" ")
                    append(actionText)

                    if (targetText.isNotBlank()) {
                        append(" ")
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(targetText)
                        }
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.width(SpaceSmall))
            Text(
                text = activity.formatedTime,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Right
            )
        }
    }
}





















