package com.madiwist.twitch.feature_post.presentation.person_list

import com.madiwist.twitch.core.domain.models.UserItem

data class PersonListState(
    val users: List<UserItem> = emptyList(),
    val isLoading: Boolean = false,
)
