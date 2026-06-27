package com.madiwist.twitch.presentation.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.madiwist.twitch.domain.models.Post
import com.madiwist.twitch.presentation.activity.ActivityScreen
import com.madiwist.twitch.presentation.chat.ChatScreen
import com.madiwist.twitch.presentation.create_post.CreatePostScreen
import com.madiwist.twitch.presentation.login.LoginScreen
import com.madiwist.twitch.presentation.main_feed.MainFeedScreen
import com.madiwist.twitch.presentation.post_detail.PostDetailsScreen
import com.madiwist.twitch.presentation.profile.ProfileScreen
import com.madiwist.twitch.presentation.register.RegisterScreen
import com.madiwist.twitch.presentation.splash.SplashScreen
import com.madiwist.twitch.presentation.search.SearchScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.ProfileScreen.route
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(Screen.MainFeedScreen.route) {
            MainFeedScreen(navController = navController)
        }
        composable(Screen.ChatScreen.route) {
            ChatScreen(navController = navController)
        }
        composable(Screen.ActivityScreen.route) {
            ActivityScreen(navController = navController)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.CreatePostScreen.route) {
            CreatePostScreen(navController = navController)
        }
        composable(Screen.PostDetailsScreen.route) {
            val post = Post(
                username = "MADI",
                imageUrl = "",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                likeCount = 23,
                commentCount = 15
            )
            PostDetailsScreen(navController = navController, post = post)
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
    }
}