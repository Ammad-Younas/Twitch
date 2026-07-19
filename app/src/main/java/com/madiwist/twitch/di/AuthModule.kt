package com.madiwist.twitch.di

import android.content.SharedPreferences
import com.madiwist.twitch.feature_auth.data.remote.AuthApi
import com.madiwist.twitch.feature_auth.data.repository.AuthRepositoryImpl
import com.madiwist.twitch.feature_auth.domain.repository.AuthRepository
import com.madiwist.twitch.feature_auth.domain.use_case.LoginUseCase
import com.madiwist.twitch.feature_auth.domain.use_case.RegisterUserCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl(AuthApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, sharedPreferences: SharedPreferences) : AuthRepository{
        return AuthRepositoryImpl(api, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideRegisterUserCase(repository: AuthRepository) : RegisterUserCase {
        return RegisterUserCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUserCase(repository: AuthRepository) : LoginUseCase {
        return LoginUseCase(repository)
    }
}