package com.madiwist.twitch.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madiwist.twitch.R

@Composable
fun BottomNavigationBar(
    content : @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val navBarHeight = 60.dp
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(5.dp),
                modifier = Modifier.offset(y = (40).dp),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
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
                BottomNavigationItem(
                    icon = Icons.Outlined.Home,
                    contentDescription = stringResource(R.string.home),
                    selected = true,
                    onClick = {}
                )
                BottomNavigationItem(
                    icon = Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = stringResource(R.string.Chat),
                    selected = false,
                    alertCount = 12,
                    onClick = {}
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {},
                    enabled = false,
                    modifier = Modifier.weight(1f)
                )

                BottomNavigationItem(
                    icon = Icons.Outlined.Notifications,
                    contentDescription = stringResource(R.string.notifications),
                    selected = false,
                    onClick = {}
                )
                BottomNavigationItem(
                    icon = Icons.Outlined.Person,
                    contentDescription = stringResource(R.string.profile),
                    selected = false,
                    onClick = {}
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            content()
        }
    }
}
