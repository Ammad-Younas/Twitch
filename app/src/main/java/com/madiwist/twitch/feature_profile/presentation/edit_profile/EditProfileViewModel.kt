package com.madiwist.twitch.feature_profile.presentation.edit_profile

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_profile.domain.model.Skill
import com.madiwist.twitch.feature_profile.domain.model.UpdateProfileData
import com.madiwist.twitch.feature_profile.domain.user_case.ProfileUserCases
import com.madiwist.twitch.feature_profile.presentation.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUserCases,
    savedStateHandle: SavedStateHandle,
    application: Application,
) : AndroidViewModel(application) {

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

    private val _skillsState = mutableStateOf(SkillsState())
    val skillsState: State<SkillsState> = _skillsState

    private val _profileState = mutableStateOf(ProfileState())
    val profileState: State<ProfileState> = _profileState

    private val _bannerImageCroppedBitmap = mutableStateOf<Bitmap?>(null)
    val bannerImageCroppedBitmap: State<Bitmap?> = _bannerImageCroppedBitmap

    private val _profilePictureCroppedBitmap = mutableStateOf<Bitmap?>(null)
    val profilePictureCroppedBitmap: State<Bitmap?> = _profilePictureCroppedBitmap

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        savedStateHandle.get<String>("userId")?.let { userId ->
            getProfile(userId)
            getSkills()
        }
    }


    private fun getProfile(userId: String) {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)
            when (val result = profileUseCase.getProfile(userId)) {
                is Resource.Success -> {
                    val profile = result.data ?: run {
                        _eventFlow.emit(
                            UiEvent.SnackbarEvent(
                                UiText.StringResource(R.string.error_couldnt_load_profile)
                            )
                        )
                        return@launch
                    }

                    _usernameState.value = usernameState.value.copy(text = profile.username)
                    _instagramState.value =
                        _instagramState.value.copy(text = profile.instagramUrl ?: "")
                    _linkedinState.value =
                        _linkedinState.value.copy(text = profile.linkedInUrl ?: "")
                    _githubState.value = _githubState.value.copy(text = profile.gitHubUrl ?: "")
                    _bioState.value = bioState.value.copy(text = profile.bio)

                    _skillsState.value = skillsState.value.copy(selectedSkills = profile.topSkillUrls)

                    _profileState.value = profileState.value.copy(
                        profile = result.data,
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    _profileState.value = profileState.value.copy(isLoading = false)
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(
                            uiText = result.uiText ?: UiText.unknownError()
                        )
                    )
                    return@launch
                }

            }
        }
    }


    private fun getSkills() {
        viewModelScope.launch {
            when (val result = profileUseCase.getSkills()) {
                is Resource.Success -> {
                    _skillsState.value = skillsState.value.copy(skills = result.data ?: run {
                        _eventFlow.emit(
                            UiEvent.SnackbarEvent(
                                uiText = UiText.StringResource(R.string.error_couldnt_load_skills)
                            )
                        )
                        return@launch
                    }
                    )
                }

                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(
                            uiText = result.uiText ?: UiText.unknownError()
                        )
                    )
                    return@launch
                }
            }
        }
    }


    private fun updateProfile(bannerImageBitmap: Bitmap, profileImageBitmap: Bitmap) {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)
            val bannerImageUri: Uri = withContext(Dispatchers.IO) {
                saveBitmapToCache("banner", bannerImageBitmap)
            }
            val profileImageUri: Uri = withContext(Dispatchers.IO) {
                saveBitmapToCache("profile", profileImageBitmap)
            }
            val result = profileUseCase.updateProfile(
                bannerUri = bannerImageUri,
                profilePictureUri = profileImageUri,
                updateProfileData = UpdateProfileData(
                    username = usernameState.value.text,
                    gitHubUrl = githubState.value.text,
                    instagramUrl = instagramState.value.text,
                    linkedInUrl = linkedinState.value.text,
                    bio = bioState.value.text,
                    skills = skillsState.value.selectedSkills
                )
            )
            when (result) {
                is Resource.Success -> {
                    _profileState.value = profileState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.SnackbarEvent(UiText.StringResource(R.string.updated_profile)))
                }
                is Resource.Error -> {
                    _profileState.value = profileState.value.copy(isLoading = false)
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(
                            uiText = result.uiText ?: UiText.unknownError()
                        )
                    )
                    return@launch
                }
            }
        }
    }


    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.EnteredUsername -> {
                _usernameState.value = usernameState.value.copy(text = event.value)
            }

            is EditProfileEvent.EnteredGithubUrl -> {
                _githubState.value = githubState.value.copy(text = event.value)
            }

            is EditProfileEvent.EnteredInstagramUrl -> {
                _instagramState.value = instagramState.value.copy(text = event.value)
            }

            is EditProfileEvent.EnteredLinkedinUrl -> {
                _linkedinState.value = linkedinState.value.copy(text = event.value)
            }

            is EditProfileEvent.EnteredBio -> {
                _bioState.value = bioState.value.copy(text = event.value)
            }

            is EditProfileEvent.CropBannerImage -> {
                _bannerImageCroppedBitmap.value = event.bitmap

            }

            is EditProfileEvent.CropProfileImage -> {
                _profilePictureCroppedBitmap.value = event.bitmap
            }

            is EditProfileEvent.SetSkillsSelected -> {
                toggleSkillSelection(event.skill)
            }

            is EditProfileEvent.UpdateProfile -> {
                val bannerCurrentBitmap = bannerImageCroppedBitmap.value
                val profileCurrentBitmap = profilePictureCroppedBitmap.value
                if (bannerCurrentBitmap == null) {
                    _eventFlow.tryEmit(
                        UiEvent.SnackbarEvent(UiText.StringResource(R.string.error_no_banner_provided))
                    )
                    return
                }
                if (profileCurrentBitmap == null) {
                    _eventFlow.tryEmit(
                        UiEvent.SnackbarEvent(UiText.StringResource(R.string.error_no_profile_provided))
                    )
                    return
                }
                updateProfile(bannerCurrentBitmap, profileCurrentBitmap)
            }
        }
    }


    private fun toggleSkillSelection(skill: Skill) {
        val currentSelectedSkills = skillsState.value.selectedSkills
        if (currentSelectedSkills.contains(skill)) {
            _skillsState.value = skillsState.value.copy(
                selectedSkills = currentSelectedSkills - skill
            )
        } else if (currentSelectedSkills.size < 3) {
            _skillsState.value = skillsState.value.copy(
                selectedSkills = currentSelectedSkills + skill
            )
        }
    }

    private fun saveBitmapToCache(name: String, bitmap: Bitmap): Uri {
        val cacheDir = getApplication<Application>().cacheDir
        val file = File(cacheDir, "${name}_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file.toUri()
    }
}