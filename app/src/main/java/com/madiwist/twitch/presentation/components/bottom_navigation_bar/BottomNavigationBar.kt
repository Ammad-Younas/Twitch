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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    val navBarHeight = 60.dp
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    if (showBottomBar){
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onFabClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.elevation(5.dp),
                    modifier = Modifier.offset(y = (40).dp),
                    shape = MaterialTheme.shapes.extraLarge,
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
                NavigationBar (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(navBarHeight + navigationBarPadding),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {

                    bottomNavItemsList.forEach { bottomNavItem ->
                        BottomNavigationItem(
                            icon = bottomNavItem.icon,
                            contentDescription = bottomNavItem.contentDescription,
                            selected = bottomNavItem.route == navController.currentDestination?.route,
                            alertCount = bottomNavItem.alertCount,
                            onClick = {
                                navController.navigate(bottomNavItem.route)
                            }
                        )
                    }
                }
            },
            modifier = modifier
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                content()
            }
        }
    } else {
        content()
    }
}
