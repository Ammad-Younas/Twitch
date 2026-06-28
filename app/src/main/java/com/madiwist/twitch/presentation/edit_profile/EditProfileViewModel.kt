package com.madiwist.twitch.presentation.edit_profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.madiwist.twitch.presentation.utils.states.TwitchTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor() : ViewModel() {


    private val _selectedSkills = mutableStateOf(setOf<String>())
    val selectedSkills: State<Set<String>> = _selectedSkills

    private val _usernameState = mutableStateOf(TwitchTextFieldState())
    val usernameState: State<TwitchTextFieldState> = _usernameState

    private val _instagramState = mutableStateOf(TwitchTextFieldState())
    val instagramState: State<TwitchTextFieldState> = _instagramState

    private val _linkedinState = mutableStateOf(TwitchTextFieldState())
    val linkedinState: State<TwitchTextFieldState> = _linkedinState

    private val _githubState = mutableStateOf(TwitchTextFieldState())

    val githubState: State<TwitchTextFieldState> = _githubState

    private val _bioState = mutableStateOf(TwitchTextFieldState())
    val bioState: State<TwitchTextFieldState> = _bioState


    fun setUsernameState(state: TwitchTextFieldState) {
        _usernameState.value = state
    }

    fun setInstagramState(state: TwitchTextFieldState) {
        _instagramState.value = state
    }

    fun setLinkedinState(state: TwitchTextFieldState) {
        _linkedinState.value = state
    }

    fun setGithubState(state: TwitchTextFieldState) {
        _githubState.value = state
    }

    fun setBioState(state: TwitchTextFieldState) {
        _bioState.value = state
    }


    fun toggleSkillSelection(skill: String) {
        val currentSet = _selectedSkills.value
        if (currentSet.contains(skill)) {
            _selectedSkills.value = currentSet - skill
        } else if (currentSet.size < 3) {
            _selectedSkills.value = currentSet + skill
        }
    }
}