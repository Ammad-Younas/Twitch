package com.madiwist.twitch.presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.madiwist.twitch.presentation.components.bottom_navigation_bar.BottomNavigationBar

@Composable
fun ProfileScreen(
    navController: NavController
) {
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Profile Screen")
    }
}