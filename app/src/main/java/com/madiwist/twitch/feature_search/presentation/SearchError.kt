package com.madiwist.twitch.feature_search.presentation

import com.madiwist.twitch.core.util.Error
import com.madiwist.twitch.core.util.UiText

data class SearchError (
    val message: UiText
) : Error()