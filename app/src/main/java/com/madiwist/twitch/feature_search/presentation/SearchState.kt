package com.madiwist.twitch.feature_search.presentation

import com.madiwist.twitch.core.domain.models.UserItem

data class SearchState(
    val isLoading: Boolean = false,
    val userItems: List<UserItem> = emptyList(),
    val ownUserId: String = ""
)
