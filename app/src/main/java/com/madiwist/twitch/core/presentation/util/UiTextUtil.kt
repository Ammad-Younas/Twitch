package com.madiwist.twitch.core.presentation.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.core.util.UiText.DynamicString
import com.madiwist.twitch.core.util.UiText.StringResource

@Composable
fun UiText.asString(): String{
    return when(this){
        is DynamicString -> this.value
        is StringResource -> stringResource(this.id)
    }
}

fun UiText.asString(context: Context): String{
    return when(this){
         is DynamicString -> this.value
        is StringResource -> context.getString(this.id)
    }
}