package com.madiwist.twitch.feature_post.presentation.create_post

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.components.CropAspectRatio
import com.madiwist.twitch.core.presentation.components.CropShape
import com.madiwist.twitch.core.presentation.components.TwitchTextField
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.components.rememberImageCropperLauncher
import com.madiwist.twitch.core.presentation.components.rememberImageCropperState
import com.madiwist.twitch.core.presentation.ui.theme.SocialIconSmall
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.presentation.util.asString
import com.madiwist.twitch.feature_post.presentation.util.PostConstants
import com.madiwist.twitch.feature_post.presentation.util.PostDescriptionError

@Composable
fun CreatePostScreen(
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: CreatePostViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val cropperState = rememberImageCropperState(
        initialAspectRatio = CropAspectRatio.Ratio16x9,
        initialShape = CropShape.RECTANGLE,
    )
    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val openGallery = rememberImageCropperLauncher(
        state = cropperState,
        onCropComplete = { bitmap ->
            croppedBitmap = bitmap
            viewModel.onEvent(CreatePostEvent.CropImage(bitmap))
        },
    )
    val createPostState = viewModel.createPostState.value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
                is UiEvent.NavigateUp -> {
                    onNavigateUp()
                }
                is UiEvent.Navigate -> {
                    onNavigate(event.route)
                }
                else -> Unit
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TwitchToolBar(
            onNavigateUp = onNavigateUp,
            modifier = Modifier.fillMaxWidth(),
            title = { Text(stringResource(R.string.create_post)) },
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
                        shape = MaterialTheme.shapes.medium,
                    )
                    .clip(
                        shape = MaterialTheme.shapes.medium,
                    )
                    .clickable { openGallery() },
                contentAlignment = Alignment.Center,
            ) {
                croppedBitmap?.let { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = stringResource(R.string.post_image),
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop,
                    )
                } ?: Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.choose_image),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Spacer(Modifier.height(SpaceMedium))

            TwitchTextField(
                text = viewModel.descriptionState.value.text,
                onValueChange = { viewModel.onEvent(CreatePostEvent.EnterDescription(it)) },
                hint = stringResource(R.string.description),
                error = when (viewModel.descriptionState.value.error) {
                    is PostDescriptionError.FieldEmpty -> stringResource(R.string.field_cant_be_empty)
                    else -> ""
                },
                minLines = 3,
                maxLines = 3,
                singleLine = false,
                maxLength = PostConstants.MAX_POST_DESCRIPTION_LENGTH
            )

            Spacer(Modifier.height(SpaceMedium))

            Button(
                onClick = { viewModel.onEvent(CreatePostEvent.PostImage) },
                modifier = Modifier.align(Alignment.End),
                enabled = !createPostState.isLoading,
            ) {
                if (createPostState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.post),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = stringResource(R.string.send),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(SocialIconSmall),
                    )
                }
            }
        }
    }
}