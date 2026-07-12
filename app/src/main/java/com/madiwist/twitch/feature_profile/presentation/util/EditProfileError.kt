package com.madiwist.twitch.feature_profile.presentation.util

import com.madiwist.twitch.core.util.Error

sealed class EditProfileError : Error() {
    object FieldEmpty: EditProfileError()

}