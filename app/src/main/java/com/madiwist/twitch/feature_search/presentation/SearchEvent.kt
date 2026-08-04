package com.madiwist.twitch.feature_search.presentation

sealed class SearchEvent {
    data class Query(val query: String) : SearchEvent()
    data class ToggleFollowState(val userId: String) : SearchEvent()
}