package com.madiwist.twitch.feature_auth.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.madiwist.twitch.R
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_auth.data.dto.request.CreateAccountRequest
import com.madiwist.twitch.feature_auth.data.dto.request.LoginRequest
import com.madiwist.twitch.feature_auth.data.remote.AuthApi
import com.madiwist.twitch.feature_auth.domain.repository.AuthRepository
import okio.IOException
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val sharedPreferences: SharedPreferences
): AuthRepository {
    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): SimpleResource {
        val request = CreateAccountRequest(email = email, username = username, password = password)
        return try {
            val response = api.register(request)
            if (response.success) {
                Resource.Success(Unit)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.unknown_error))
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server),
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_something_went_wrong)
            )
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): SimpleResource {
        val request = LoginRequest(email = email, password = password)
        return try {
            val response = api.login(request)
            if (response.success) {
                response.data?.token?.let { token ->
                    sharedPreferences.edit {
                        putString(Constants.KEY_JWT_TOKEN, token)
                    }
                }
                Resource.Success(Unit)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.unknown_error))
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server),
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.invalid_credential)
            )
        }
    }
}