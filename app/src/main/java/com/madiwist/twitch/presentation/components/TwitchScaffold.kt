package com.madiwist.twitch.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.madiwist.twitch.domain.models.BottomNavItem
import com.madiwist.twitch.presentation.utils.NavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwitchScaffold(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentRoute: String? = null,
    showBottomBarAndFab: Boolean = true,
    bottomNavItemsList: List<BottomNavItem> = NavItems.NAV_ITEMS,
    onFabClick: () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val cutoutShape = remember(density) {
        GenericShape { size, _ ->
            val cutoutRadius = with(density) { 38.dp.toPx() }
            val cornerRadius = with(density) { 12.dp.toPx() }

            val cutoutCenterX = size.width / 2f

            val notchStart = cutoutCenterX - cutoutRadius - cornerRadius
            val notchEnd = cutoutCenterX + cutoutRadius + cornerRadius

            addPath(Path().apply {
                moveTo(0f, cornerRadius)
                quadraticTo(0f, 0f, cornerRadius, 0f)
                lineTo(notchStart, 0f)
                cubicTo(
                    x1 = cutoutCenterX - cutoutRadius, y1 = 0f,
                    x2 = cutoutCenterX - cutoutRadius, y2 = cutoutRadius,
                    x3 = cutoutCenterX, y3 = cutoutRadius
                )
                cubicTo(
                    x1 = cutoutCenterX + cutoutRadius, y1 = cutoutRadius,
                    x2 = cutoutCenterX + cutoutRadius, y2 = 0f,
                    x3 = notchEnd, y3 = 0f
                )
                lineTo(size.width - cornerRadius, 0f)
                quadraticTo(size.width, 0f, size.width, cornerRadius)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            })
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom),
        topBar = topBar,
        bottomBar = {
            if (showBottomBarAndFab) {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(cutoutShape)
                        .windowInsetsPadding(WindowInsets.safeDrawing
                            .only(WindowInsetsSides.Bottom)),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp,
                    ) {
                        bottomNavItemsList.forEach { bottomNavItem ->
                            BottomNavigationItem(
                                icon = bottomNavItem.icon,
                                contentDescription = bottomNavItem.contentDescription,
                                selected = bottomNavItem.route == currentRoute,
                                alertCount = bottomNavItem.alertCount,
                                onClick = {
                                    bottomNavItem.route?.let { route ->
                                        if (route != currentRoute) {
                                            navController.navigate(route)
                                        }
                                    }
                                },
                                enabled = bottomNavItem.icon != null,
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            if (showBottomBarAndFab){
                TwitchFab (
                    onClick = onFabClick
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content()
        }
    }
}