package com.madiwist.twitch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.madiwist.twitch.presentation.components.TwitchScaffold
import com.madiwist.twitch.presentation.ui.theme.TwitchTheme
import com.madiwist.twitch.presentation.utils.Navigation
import com.madiwist.twitch.presentation.utils.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            TwitchTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TwitchScaffold(
                        navController = navController,
                        showBottomBarAndFab = currentRoute in listOf(
                            Screen.MainFeedScreen.route,
                            Screen.ChatScreen.route,
                            Screen.ActivityScreen.route,
                            Screen.ProfileScreen.route
                        ),
                        modifier = Modifier.fillMaxSize(),
                        onFabClick = {
                            navController.navigate(Screen.CreatePostScreen.route)
                        },
                        currentRoute = currentRoute,
                        toolBarTitle = "Feed"
                    ) {
                        Navigation(navController = navController)
                    }
                }
            }
        }
    }
}