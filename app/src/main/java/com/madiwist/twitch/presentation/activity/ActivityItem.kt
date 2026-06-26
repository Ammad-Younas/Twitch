package com.madiwist.twitch.presentation.activity

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
import com.madiwist.twitch.domain.models.Activity
import com.madiwist.twitch.domain.util.ActivityAction
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.presentation.ui.theme.SpaceSmall

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
                    when(activity.actionType){
                        is ActivityAction.LikedPost -> {
                            append(stringResource(R.string.liked_post))
                        }
                        is ActivityAction.CommentedOnPost -> {
                            append(stringResource(R.string.commented_on_post))
                        }
                    }
                    append(" ")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(stringResource(R.string.your_post))
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





















