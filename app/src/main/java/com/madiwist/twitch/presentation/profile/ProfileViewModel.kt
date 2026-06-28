package com.madiwist.twitch.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _toolBarOffsetY = mutableFloatStateOf(0f)
    val toolBarOffsetY: State<Float> = _toolBarOffsetY

    private val _expandedRatio = mutableFloatStateOf(1f)
    val expandedRatio: State<Float> = _expandedRatio


    fun setExpandedRatio(ratio: Float) {
        _expandedRatio.floatValue = ratio
    }

    fun setToolbarOffsetY(value: Float){
        _toolBarOffsetY.floatValue = value
    }
 
}