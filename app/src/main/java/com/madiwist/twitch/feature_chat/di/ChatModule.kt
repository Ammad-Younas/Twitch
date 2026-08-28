package com.madiwist.twitch.feature_chat.di

import com.madiwist.twitch.feature_chat.data.remote.ChatApi
import com.madiwist.twitch.feature_chat.data.remote.util.ChatWebSocketClient
import com.madiwist.twitch.feature_chat.data.repository.ChatRepositoryImpl
import com.madiwist.twitch.feature_chat.domain.repository.ChatRepository
import com.madiwist.twitch.feature_chat.domain.use_case.ChatUseCases
import com.madiwist.twitch.feature_chat.domain.use_case.DisconnectChat
import com.madiwist.twitch.feature_chat.domain.use_case.GetChatsForUser
import com.madiwist.twitch.feature_chat.domain.use_case.GetMessagesForChat
import com.madiwist.twitch.feature_chat.domain.use_case.InitializeChat
import com.madiwist.twitch.feature_chat.domain.use_case.ObserveChatEvents
import com.madiwist.twitch.feature_chat.domain.use_case.ObserveMessages
import com.madiwist.twitch.feature_chat.domain.use_case.SendMessage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatApi(okHttpClient: OkHttpClient): ChatApi {
        return Retrofit.Builder()
            .baseUrl(ChatApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(okHttpClient: OkHttpClient, json: Json): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                preconfigured = okHttpClient
            }
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(json)
            }
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    @Provides
    @Singleton
    fun provideChatRepository(api: ChatApi, client: ChatWebSocketClient): ChatRepository {
        return ChatRepositoryImpl(api, client)
    }

    @Provides
    @Singleton
    fun provideChatWebSocketClient(client: HttpClient, json: Json): ChatWebSocketClient {
        return ChatWebSocketClient(client, json)
    }

    @Provides
    @Singleton
    fun provideChatUseCases(repository: ChatRepository): ChatUseCases {
        return ChatUseCases(
            sendMessage = SendMessage(repository),
            observeMessages = ObserveMessages(repository),
            observeChatEvents = ObserveChatEvents(repository),
            initializeChat = InitializeChat(repository),
            disconnectChat = DisconnectChat(repository),
            getChatsForUser = GetChatsForUser(repository),
            getMessagesForChat = GetMessagesForChat(repository)
        )
    }
}
