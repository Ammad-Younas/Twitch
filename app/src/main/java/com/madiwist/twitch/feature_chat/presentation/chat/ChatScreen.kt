package com.madiwist.twitch.feature_chat.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.feature_chat.presentation.chat.component.ChatItem

@Composable
fun ChatScreen(
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {

    val chats = viewModel.chatState.value.chats
    val isLoading = viewModel.chatState.value.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TwitchToolBar(
            onNavigateUp = onNavigateUp,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    text = stringResource(id = R.string.chats),
                    fontWeight = FontWeight.Bold,
                )
            },
            showBackArrow = false,
        )
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(SpaceSmall)
                ) {
                    items(chats) { chat ->
                        ChatItem(
                            item = chat,
                            onItemClick = {
                                val encodedUrl = URLEncoder.encode(chat.remoteUserProfileUrl, StandardCharsets.UTF_8.toString())
                                onNavigate(Screen.MessageScreen.route + "/${chat.chatId}/${chat.remoteUserId}/${chat.remoteUsername}/$encodedUrl")
                            }
                        )
                    }
                }
            }
        }
    }
}