package com.madiwist.twitch.feature_post.presentation.person_list

import com.madiwist.twitch.core.domain.models.UserItem

data class PersonListState(
    val users: List<UserItem> = emptyList(),
    val ownUserId: String = "",
    val isLoading: Boolean = false,
)
