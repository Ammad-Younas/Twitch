package com.madiwist.twitch.feature_auth.domain.repository

import com.madiwist.twitch.core.util.SimpleResource

interface AuthRepository {
    suspend fun register(email: String, username: String, password: String) : SimpleResource
    suspend fun login(email: String, password: String) : SimpleResource
}