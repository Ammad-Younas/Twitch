package com.madiwist.twitch.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import com.madiwist.twitch.R
import com.madiwist.twitch.domain.models.BottomNavItem

object NavItems {
    val NAV_ITEMS = listOf(
        BottomNavItem(
            route = Screen.MainFeedScreen.route,
            icon = Icons.Outlined.Home,
            contentDescription = R.string.home
        ),
        BottomNavItem(
            route = Screen.ChatScreen.route,
            icon = Icons.AutoMirrored.Outlined.Chat,
            contentDescription = R.string.chat
        ),
        BottomNavItem(
            route = Screen.ActivityScreen.route,
            icon = Icons.Outlined.Notifications,
            contentDescription = R.string.activity
        ),
        BottomNavItem(
            route = Screen.ProfileScreen.route,
            icon = Icons.Outlined.Person,
            contentDescription = R.string.profile
        ),
    )
}