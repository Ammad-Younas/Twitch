package com.madiwist.twitch.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium
import kotlin.jvm.Throws

@Composable
@Throws(IllegalArgumentException::class)
fun RowScope.BottomNavigationItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    contentDescription: Int? = null,
    selected: Boolean = false,
    alertCount: Int? = null,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick : () -> Unit,
    enabled: Boolean = true,
    iconSize: Dp = 30.dp
) {
    if (alertCount != null && alertCount < 0) {
        throw IllegalArgumentException("Alert Count can't be negative")
    }

    val lineLength = animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300
        )
    )

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = selectedColor,
            unselectedIconColor = unselectedColor,
            indicatorColor = Color.Transparent,
        ),
        icon = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(SpaceMedium)
                    .drawBehind {
                        if (lineLength.value > 0f) {
                            drawLine(
                                color = selectedColor,
                                start = Offset(size.width / 2f - lineLength.value * 10.dp.toPx(), size.height),
                                end = Offset(size.width / 2f + lineLength.value * 10.dp.toPx(), size.height),
                                strokeWidth = 3.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }
            ) {
                if (icon != null){
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription.toString(),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(iconSize)
                    )
                }
                if (alertCount != null && alertCount > 0) {
                    val alertText = if (alertCount > 99) "99+" else alertCount.toString()
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(25.dp)
                            .offset(x = 15.dp, y = (-20).dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .align(Alignment.Center)
                    ) {
                        Text(
                            text = alertText,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.offset(y = (-1f).dp)
                        )
                    }
                }
            }
        }
    )
}
