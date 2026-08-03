package com.madiwist.twitch.feature_profile.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.domain.models.UserItem
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_profile.domain.model.Profile
import com.madiwist.twitch.feature_profile.domain.model.Skill
import com.madiwist.twitch.feature_profile.domain.model.UpdateProfileData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getPostsPaged(userId: String) : Flow<PagingData<Post>>

    suspend fun getProfile(userId: String): Resource<Profile>
    suspend fun getSkills(): Resource<List<Skill>>
    suspend fun searchUser(query: String) : Resource<List<UserItem>>
    suspend fun updateProfile(
        bannerImageUri: Uri?,
        profileImageUri: Uri?,
        userProfileData: UpdateProfileData
    ) : SimpleResource
}