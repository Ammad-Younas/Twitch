package com.madiwist.twitch.domain.util

sealed class ActivityAction {
    object LikedPost : ActivityAction()
    object CommentedOnPost: ActivityAction()
}