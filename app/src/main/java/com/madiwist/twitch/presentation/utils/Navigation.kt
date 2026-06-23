package com.madiwist.twitch.presentation.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.madiwist.twitch.presentation.activity.ActivityScreen
import com.madiwist.twitch.presentation.chat.ChatScreen
import com.madiwist.twitch.presentation.login.LoginScreen
import com.madiwist.twitch.presentation.main_feed.MainFeedScreen
import com.madiwist.twitch.presentation.profile.ProfileScreen
import com.madiwist.twitch.presentation.register.RegisterScreen
import com.madiwist.twitch.presentation.splash.SplashScreen

@Composable
fun Navigation(navController: NavHostController) {
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
        composable(Screen.ChatScreen.route){
            ChatScreen(navController = navController)
        }
        composable(Screen.ActivityScreen.route){
            ActivityScreen(navController = navController)
        }
        composable(Screen.ProfileScreen.route){
            ProfileScreen(navController = navController)
        }
    }
}