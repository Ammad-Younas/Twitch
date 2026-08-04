package com.madiwist.twitch.core.presentation

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.madiwist.twitch.core.presentation.components.TwitchScaffold
import com.madiwist.twitch.core.presentation.navigation.Navigation
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.ui.theme.TwitchTheme
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashViewModel.authenticate()

        splashScreen.setKeepOnScreenCondition {
            !splashViewModel.isAuthComplete.value
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            val startDestination by splashViewModel.authDestination.collectAsState()

            if (startDestination != null) {
                TwitchTheme {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val snackbarHostState = remember { SnackbarHostState() }

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        TwitchScaffold(
                            showBottomBarAndFab = when (currentRoute?.split("?")?.get(0)) {
                                Screen.MainFeedScreen.route,
                                Screen.ChatScreen.route,
                                Screen.ActivityScreen.route -> true
                                Screen.ProfileScreen.route -> {
                                    val userId = navBackStackEntry?.arguments?.getString("userId")
                                    val ownUserId = sharedPreferences.getString(Constants.KEY_USER_ID, "")
                                    userId == null || userId == ownUserId
                                }
                                else -> false
                            },
                            modifier = Modifier.fillMaxSize(),
                            onFabClick = {
                                navController.navigate(Screen.CreatePostScreen.route)
                            },
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = route != Screen.MainFeedScreen.route
                                    }
                                    launchSingleTop = true
                                    restoreState = route != Screen.MainFeedScreen.route
                                }
                            },
                            currentRoute = currentRoute,
                            snackbarHostState = snackbarHostState
                        ) {
                            Navigation(
                                navController = navController,
                                snackbarHostState = snackbarHostState,
                                startDestination = startDestination!!
                            )
                        }
                    }
                }
            }
        }
    }
}