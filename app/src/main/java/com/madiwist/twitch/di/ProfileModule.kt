package com.madiwist.twitch.di

import android.content.SharedPreferences
import com.google.gson.Gson
import com.madiwist.twitch.core.data.repository.ProfileRepositoryImpl
import com.madiwist.twitch.core.domain.use_case.ToggleFollowStateForUserUseCase
import com.madiwist.twitch.feature_post.data.remote.PostApi
import com.madiwist.twitch.feature_profile.data.remote.ProfileApi
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository
import com.madiwist.twitch.feature_profile.domain.user_case.GetPostsForProfileUserCase
import com.madiwist.twitch.feature_profile.domain.user_case.GetProfileUseCase
import com.madiwist.twitch.feature_profile.domain.user_case.GetSkillUseCase
import com.madiwist.twitch.feature_profile.domain.user_case.ProfileUserCases
import com.madiwist.twitch.feature_profile.domain.user_case.SearchUserUseCase
import com.madiwist.twitch.feature_profile.domain.user_case.SetSkillSelectedUseCase
import com.madiwist.twitch.feature_profile.domain.user_case.UpdateProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileApi(client: OkHttpClient) : ProfileApi {
        return Retrofit.Builder()
            .baseUrl(ProfileApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        profileApi: ProfileApi,
        postApi: PostApi,
        gson: Gson,
        sharedPreferences: SharedPreferences
    ) : ProfileRepository {
        return ProfileRepositoryImpl(profileApi, postApi, gson, sharedPreferences)
    }


    @Provides
    @Singleton
    fun provideProfileUseCase(repository: ProfileRepository) : ProfileUserCases {
        return ProfileUserCases(
            getProfile = GetProfileUseCase(repository),
            getSkills = GetSkillUseCase(repository),
            updateProfile = UpdateProfileUseCase(repository),
            setSkills = SetSkillSelectedUseCase(),
            getPosts = GetPostsForProfileUserCase(repository),
            searchUser = SearchUserUseCase(repository),
            toggleFollowStateForUser = ToggleFollowStateForUserUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideToggleFollowForUseCase(repository: ProfileRepository) : ToggleFollowStateForUserUseCase {
        return ToggleFollowStateForUserUseCase(repository)
    }

}