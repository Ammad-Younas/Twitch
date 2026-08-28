package com.madiwist.twitch.feature_chat.presentation.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.components.BrokenImage
import com.madiwist.twitch.core.presentation.components.SendTextField
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.ui.theme.Shapes
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.core.presentation.util.ErrorImageLoading
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_chat.presentation.message.component.OwnMessage
import com.madiwist.twitch.feature_chat.presentation.message.component.RemoteMessage
import com.madiwist.twitch.core.presentation.navigation.Screen

@Composable
fun MessageScreen(
    remoteUsername: String,
    remoteUserProfilePictureUrl: String,
    remoteUserId: String,
    onNavigateUp: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    viewModel: MessageViewModel = hiltViewModel()
) {

    val messageState by viewModel.messageState

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TwitchToolBar(
            onNavigateUp = onNavigateUp,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = SpaceMedium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(remoteUserProfilePictureUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(Shapes.extraLarge)
                            .size(Constants.PROFILE_PICTURE_SIZE_LARGE - 85.dp),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            )
                            {
                                CircularProgressIndicator()
                            }
                        },
                        error = {
                            BrokenImage(
                                modifier = Modifier.fillMaxSize(),
                                errorImageLoading = ErrorImageLoading.PROFILE_TYPE
                            )
                        }
                    )
                    Spacer(Modifier.width(SpaceMedium))
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onNavigate(Screen.ProfileScreen.route + "?userId=$remoteUserId")
                            },
                        text = remoteUsername,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            showBackArrow = true,
        )
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(SpaceSmall),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    reverseLayout = true,
                ) {
                    itemsIndexed(messageState.messages) { i, message ->
                        if (i >= messageState.messages.size - 1 && !messageState.isLoading && !messageState.endReached) {
                            viewModel.loadNextMessages()
                        }
                        val isOwnMessage = message.fromId == messageState.ownUserId
                        if (isOwnMessage) {
                            OwnMessage(
                                message = message.text,
                                timestamp = message.timestamp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            RemoteMessage(
                                message = message.text,
                                timestamp = message.timestamp
                            )
                        }
                        Spacer(modifier = Modifier.height(SpaceMedium))
                    }
                }
                if (messageState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            SendTextField(
                state = viewModel.messageTextFieldState.value,
                onValueChange = {
                    viewModel.onEvent(MessageEvent.EnteredMessage(it))
                },
                hint = stringResource(R.string.type_your_message),
                onSend = {
                    viewModel.onEvent(MessageEvent.SendMessage)
                },
            )
        }
    }
}