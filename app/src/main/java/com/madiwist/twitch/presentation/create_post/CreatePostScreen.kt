package com.madiwist.twitch.presentation.create_post

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.presentation.components.TwitchTextField
import com.madiwist.twitch.presentation.components.TwitchToolBar
import com.madiwist.twitch.presentation.ui.theme.SocialIconSmall
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.presentation.utils.states.TwitchTextFieldState

@Composable
fun CreatePostScreen(
    navController: NavController,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TwitchToolBar(
            navController = navController,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(stringResource(R.string.create_post))
            },
            showBackArrow = true,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .verticalScroll(rememberScrollState())
                .padding(SpaceMedium)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable {

                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.choose_image),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(Modifier.height(SpaceMedium))
            TwitchTextField(
                text = viewModel.descriptionState.value.text,
                onValueChange = {
                    viewModel.setDescriptionState(TwitchTextFieldState(text = it))
                },
                hint = stringResource(R.string.description),
                error = viewModel.descriptionState.value.error,
                minLines = 3,
                maxLines = 3,
                singleLine = false
            )
            Spacer(Modifier.height(SpaceMedium))
            Button(
                onClick = {},
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(
                    stringResource(R.string.post),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = stringResource(R.string.send),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(SocialIconSmall)
                )
            }
        }
    }
}