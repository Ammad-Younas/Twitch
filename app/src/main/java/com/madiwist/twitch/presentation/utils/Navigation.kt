package com.madiwist.twitch.presentation.utils

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madiwist.twitch.presentation.login.LoginScreen
import com.madiwist.twitch.presentation.main_feed.MainFeedScreen
import com.madiwist.twitch.presentation.register.RegisterScreen
import com.madiwist.twitch.presentation.splash.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost (
        navController = navController,
        startDestination = Screen.SplashScreen.route
    )  {
        composable(Screen.SplashScreen.route){
            SplashScreen(navController = navController)
        }
        composable(Screen.LoginScreen.route){
            LoginScreen(navController = navController)
        }
        composable(Screen.RegisterScreen.route){
            RegisterScreen(navController = navController)
        }
        composable(Screen.MainFeedScreen.route){
            MainFeedScreen(navController = navController)
        }
    }
}