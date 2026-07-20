package com.madiwist.twitch.core.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.feature_activity.presentation.activity.ActivityScreen
import com.madiwist.twitch.feature_auth.presentation.login.LoginScreen
import com.madiwist.twitch.feature_auth.presentation.register.RegisterScreen
import com.madiwist.twitch.feature_chat.presentation.chat.ChatScreen
import com.madiwist.twitch.feature_post.presentation.create_post.CreatePostScreen
import com.madiwist.twitch.feature_post.presentation.main_feed.MainFeedScreen
import com.madiwist.twitch.feature_post.presentation.person_list.PersonListScreen
import com.madiwist.twitch.feature_post.presentation.post_detail.PostDetailsScreen
import com.madiwist.twitch.feature_profile.presentation.edit_profile.EditProfileScreen
import com.madiwist.twitch.feature_profile.presentation.profile.ProfileScreen
import com.madiwist.twitch.feature_search.presentation.SearchScreen
import com.madiwist.twitch.feature_splash.SplashScreen

@Composable
fun Navigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                navController = navController
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.MainFeedScreen.route) {
            MainFeedScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
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
                timestamp = System.currentTimeMillis(),
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
        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(navController = navController)
        }
        composable(Screen.PersonListScreen.route) {
            PersonListScreen(navController = navController)
        }

    }
}