package com.madiwist.twitch.feature_chat.di

import android.app.Application
import com.google.gson.Gson
import com.madiwist.twitch.feature_chat.data.remote.ws.util.CustomGsonMessageAdapter
import com.madiwist.twitch.feature_chat.data.remote.ws.util.FlowStreamAdapter
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideScarlet(application: Application, gson: Gson, client: OkHttpClient): Scarlet {
        return Scarlet.Builder()
            .addMessageAdapterFactory(CustomGsonMessageAdapter.Factory(gson))
            .addStreamAdapterFactory(FlowStreamAdapter.Factory)
            .webSocketFactory(client.newWebSocketFactory(url = "ws://192.168.100.135:8001/api/chat/websocket"))
            .lifecycle(AndroidLifecycle.ofApplicationForeground(application))
            .build()
    }

}