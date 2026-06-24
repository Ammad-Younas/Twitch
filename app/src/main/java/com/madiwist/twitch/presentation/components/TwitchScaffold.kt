package com.madiwist.twitch.presentation.components

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.domain.models.BottomNavItem
import com.madiwist.twitch.presentation.utils.NavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwitchScaffold(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentRoute: String?,
    toolBarTitle: String? = null,
    showBottomBarAndFab: Boolean = true,
    showToolBar: Boolean = true,
    showBackArrow: Boolean = true,
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (showToolBar){
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    title = {
                        if (toolBarTitle != null){
                            Text(
                                text = toolBarTitle,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        if (showBackArrow){
                            IconButton(
                                onClick = { navController.popBackStack() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.Back)
                                )
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (showBottomBarAndFab){
                FloatingActionButton(
                    onClick = onFabClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.elevation(12.dp),
                    shape = CircleShape,
                    modifier = Modifier.offset(y = 45.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.make_post),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if (showBottomBarAndFab){
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
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            content()
        }
    }
}