package com.madiwist.twitch.domain.models

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String?,
    val icon : ImageVector?,
    val contentDescription: Int? = null,
    val alertCount: Int? = null
)
