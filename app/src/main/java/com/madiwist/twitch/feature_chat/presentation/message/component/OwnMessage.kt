package com.madiwist.twitch.feature_chat.presentation.message.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.madiwist.twitch.core.presentation.ui.theme.ExtraSpaceSmall
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall

@Composable
fun OwnMessage(
    message: String,
    timestamp: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    triangleWidth: Dp = 30.dp,
    triangleHeight: Dp = 30.dp
) {
    val cornerRadius = MaterialTheme.shapes.medium.bottomEnd
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(ExtraSpaceSmall)
    ) {
        Text(
            text = timestamp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Bottom)
        )
        Spacer(Modifier.width(SpaceSmall))
        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(SpaceMedium)
                .drawBehind {
                    val cornerRadiusPx =
                        cornerRadius.toPx(shapeSize = size, density = Density(density))
                    val path = Path().apply {
                        moveTo(
                            x = size.width,
                            y = size.height - cornerRadiusPx
                        )
                        lineTo(
                            x = size.width,
                            y = size.height + triangleHeight.toPx()
                        )
                        lineTo(
                            x = size.width - triangleWidth.toPx(),
                            y = size.height - cornerRadiusPx
                        )
                        close()
                    }
                    drawPath(
                        path = path,
                        color = color
                    )
                }
        ) {
            Text(
                text = message
            )
        }


    }
}