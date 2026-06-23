package com.madiwist.twitch.presentation.components.bottom_navigation_bar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.domain.models.BottomNavItem
import com.madiwist.twitch.presentation.components.BottomNavigationItem
import com.madiwist.twitch.presentation.utils.NavItems

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    showBottomBar: Boolean = true,
    bottomNavItemsList: List<BottomNavItem> = NavItems.NAV_ITEMS,
    onFabClick: () -> Unit = {},
    content : @Composable () -> Unit
) {
    val navBarHeight = 50.dp
    val density = LocalDensity.current
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val cutoutShape = remember(density) {
        GenericShape { size, _ ->
            val cutoutRadius = with(density) { 38.dp.toPx() }
            val cutoutCenterX = size.width / 2f
            
            addPath(Path().apply {
                moveTo(0f, 0f)
                lineTo(cutoutCenterX - cutoutRadius, 0f)
                arcTo(
                    rect = Rect(
                        left = cutoutCenterX - cutoutRadius,
                        top = -cutoutRadius,
                        right = cutoutCenterX + cutoutRadius,
                        bottom = cutoutRadius
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = -180f,
                    forceMoveTo = false
                )
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            })
        }
    }

    if (showBottomBar){
        Scaffold(
            modifier = modifier,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onFabClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.elevation(5.dp),
                    shape = CircleShape,
                    modifier = Modifier.offset(y = 45.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.make_post),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(navBarHeight + navigationBarPadding)
                        .clip(cutoutShape),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {
                    NavigationBar (
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp,
                        windowInsets = WindowInsets(0, 0, 0, 0)
                    ) {
                        bottomNavItemsList.forEach { bottomNavItem ->
                            BottomNavigationItem(
                                icon = bottomNavItem.icon,
                                contentDescription = bottomNavItem.contentDescription,
                                selected = bottomNavItem.route == navController.currentBackStackEntry?.destination?.route,
                                alertCount = bottomNavItem.alertCount,
                                onClick = {
                                    bottomNavItem.route?.let { route ->
                                        if (route != navController.currentBackStackEntry?.destination?.route) {
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
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                content()
            }
        }
    } else {
        content()
    }
}
