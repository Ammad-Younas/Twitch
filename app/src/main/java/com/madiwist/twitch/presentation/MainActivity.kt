package com.madiwist.twitch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.madiwist.twitch.presentation.components.bottom_navigation_bar.BottomNavigationBar
import com.madiwist.twitch.presentation.ui.theme.TwitchTheme
import com.madiwist.twitch.presentation.utils.Navigation
import com.madiwist.twitch.presentation.utils.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TwitchTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    innerPadding ->
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        BottomNavigationBar(
                            navController = navController,
                            showBottomBarAndFab = navBackStackEntry?.destination?.route in listOf(
                                Screen.MainFeedScreen.route,
                                Screen.ChatScreen.route,
                                Screen.ActivityScreen.route,
                                Screen.ProfileScreen.route

                            ),
                            modifier = Modifier.fillMaxSize(),
                            onFabClick = {
                                navController.navigate(Screen.CreatePostScreen.route)
                            }
                        ) {
                            Navigation(navController = navController)
                        }
                    }
                }
            }
        }
    }
}