package com.madiwist.twitch.feature_post.presentation.person_list

sealed class PersonListEvent {
    data class ToggleFollowState(val userId: String) : PersonListEvent()
}