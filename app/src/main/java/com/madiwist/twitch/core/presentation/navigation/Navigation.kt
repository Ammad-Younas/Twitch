package com.madiwist.twitch.core.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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

@Composable
fun Navigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                onNavigate = navController::navigate,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(
                onPopBackStack = navController::popBackStack,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.MainFeedScreen.route) {
            MainFeedScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.ChatScreen.route) {
            ChatScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
            )
        }
        composable(Screen.ActivityScreen.route) {
            ActivityScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
            )
        }
        composable(
            route = Screen.ProfileScreen.route + "?userId={userId}",
            arguments = listOf(
                navArgument(name = "userId"){
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            ProfileScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                snackbarHostState = snackbarHostState,
            )
        }
        composable(Screen.CreatePostScreen.route) {
            CreatePostScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                snackbarHostState = snackbarHostState,
            )
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
            PostDetailsScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                post = post
            )
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
            )
        }
        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
            )
        }
        composable(Screen.PersonListScreen.route) {
            PersonListScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
            )
        }
    }
}