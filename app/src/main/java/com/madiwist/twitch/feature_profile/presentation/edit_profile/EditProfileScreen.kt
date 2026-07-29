package com.madiwist.twitch.feature_profile.presentation.edit_profile

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.components.CropAspectRatio
import com.madiwist.twitch.core.presentation.components.CropShape
import com.madiwist.twitch.core.presentation.components.TwitchTextField
import com.madiwist.twitch.core.presentation.components.TwitchToolBar
import com.madiwist.twitch.core.presentation.components.rememberImageCropperLauncher
import com.madiwist.twitch.core.presentation.components.rememberImageCropperState
import com.madiwist.twitch.core.presentation.ui.theme.ExtraSpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.presentation.util.asString
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_profile.presentation.edit_profile.components.SkillsChips
import com.madiwist.twitch.feature_profile.presentation.util.EditProfileError

@Composable
fun EditProfileScreen(
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    snackbarHostState: SnackbarHostState,
    viewModel: EditProfileViewModel = hiltViewModel()
) {

    val bannerCropperState = rememberImageCropperState(
        initialAspectRatio = CropAspectRatio.Ratio16x9,
        initialShape = CropShape.RECTANGLE,
    )
    var bannerCroppedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val bannerOpenGallery = rememberImageCropperLauncher(
        state = bannerCropperState,
        onCropComplete = { bitmap ->
            bannerCroppedBitmap = bitmap
            viewModel.onEvent(EditProfileEvent.CropBannerImage(bitmap))
        },
    )


    val profileImageCropperState = rememberImageCropperState(
        initialAspectRatio = CropAspectRatio.Square,
        initialShape = CropShape.CIRCLE,
    )
    var profileImageCroppedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val profileOpenGallery = rememberImageCropperLauncher(
        state = profileImageCropperState,
        onCropComplete = { bitmap ->
            profileImageCroppedBitmap = bitmap
            viewModel.onEvent(EditProfileEvent.CropProfileImage(bitmap))
        },
    )

    val profileState = viewModel.profileState.value

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
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TwitchToolBar(
            onNavigateUp = onNavigateUp,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(stringResource(R.string.edit_profile))
            },
            showBackArrow = true,
            navActions = {
                IconButton(
                    onClick = {
                        viewModel.onEvent(EditProfileEvent.UpdateProfile)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Done,
                        contentDescription = stringResource(R.string.done),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .verticalScroll(rememberScrollState())
        ) {
            BannerEditSection(
                bannerImage = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(bannerCroppedBitmap ?: profileState.profile?.bannerUrl)
                        .crossfade(true)
                        .build()
                ),
                profileImage = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profileImageCroppedBitmap ?: profileState.profile?.profilePictureUrl)
                        .crossfade(true)
                        .build()
                ),
                onBannerClick = {
                    bannerOpenGallery()
                },
                onProfileImageClick = {
                    profileOpenGallery()
                }
            )
            Spacer(Modifier.height(SpaceLarge))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpaceLarge)
            ) {
                TwitchTextField(
                    text = viewModel.usernameState.value.text,
                    onValueChange = {
                        viewModel.onEvent(EditProfileEvent.EnteredUsername(it))
                    },
                    hint = stringResource(R.string.username),
                    error = when(viewModel.usernameState.value.error){
                        is EditProfileError.FieldEmpty -> stringResource(R.string.field_cant_be_empty)
                        else -> ""
                    },
                    leadingIcon = Icons.Filled.Person
                )
                Spacer(Modifier.height(SpaceLarge))
                TwitchTextField(
                    text = viewModel.instagramState.value.text,
                    onValueChange = {
                        viewModel.onEvent(EditProfileEvent.EnteredInstagramUrl(it))
                    },
                    hint = stringResource(R.string.instagram),
                    error = when(viewModel.instagramState.value.error){
                        is EditProfileError.FieldEmpty -> stringResource(R.string.field_cant_be_empty)
                        else -> ""
                    },
                    leadingIcon = ImageVector.vectorResource(
                        R.drawable.instagram,
                    )
                )
                Spacer(Modifier.height(SpaceLarge))
                TwitchTextField(
                    text = viewModel.linkedinState.value.text,
                    onValueChange = {
                        viewModel.onEvent(EditProfileEvent.EnteredLinkedinUrl(it))
                    },
                    hint = stringResource(R.string.linkedin),
                    error = when(viewModel.linkedinState.value.error){
                        is EditProfileError.FieldEmpty -> stringResource(R.string.field_cant_be_empty)
                        else -> ""
                    },
                    leadingIcon = ImageVector.vectorResource(R.drawable.linkedin)
                )
                Spacer(Modifier.height(SpaceLarge))
                TwitchTextField(
                    text = viewModel.githubState.value.text,
                    onValueChange = {
                        viewModel.onEvent(EditProfileEvent.EnteredGithubUrl(it))
                    },
                    hint = stringResource(R.string.github),
                    error = when(viewModel.githubState.value.error){
                        is EditProfileError.FieldEmpty -> stringResource(R.string.field_cant_be_empty)
                        else -> ""
                    },
                    leadingIcon = ImageVector.vectorResource(R.drawable.github)
                )
                Spacer(Modifier.height(SpaceLarge))
                TwitchTextField(
                    text = viewModel.bioState.value.text,
                    onValueChange = {
                        viewModel.onEvent(EditProfileEvent.EnteredBio(it))
                    },
                    hint = stringResource(R.string.bio),
                    error = when(viewModel.bioState.value.error){
                        is EditProfileError.FieldEmpty -> stringResource(R.string.field_cant_be_empty)
                        else -> ""
                    },
                    minLines = 3,
                    maxLines = 3,
                    singleLine = false
                )
                Spacer(Modifier.height(ExtraSpaceLarge))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.select_your_top_skills),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpaceLarge),
                    horizontalArrangement = Arrangement.spacedBy(SpaceLarge, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(SpaceSmall)
                ) {

                    val selectedSkills = viewModel.skillsState.value.selectedSkills
                    val hasReachedLimit = selectedSkills.size >= 3

                    viewModel.skillsState.value.skills.forEach { skill ->

                        val isSelected = skill in selectedSkills

                        SkillsChips(
                            text = skill.name,
                            selected = isSelected,
                            enabled = isSelected || !hasReachedLimit,
                            onChipClick = {
                                viewModel.onEvent(EditProfileEvent.SetSkillsSelected(skill))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BannerEditSection(
    bannerImage: Painter,
    profileImage: Painter,
    onBannerClick: () -> Unit = {},
    onProfileImageClick: () -> Unit = {},
) {
    val containerWidth = LocalWindowInfo.current.containerSize.width
    val bannerHeight = with(LocalDensity.current) { (containerWidth.toDp() / 2.5f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(bannerHeight + Constants.PROFILE_PICTURE_SIZE_LARGE / 2f),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = bannerImage,
            contentDescription = stringResource(R.string.banner_image),
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
                .clickable {
                    onBannerClick()
                },
            contentScale = ContentScale.Crop
        )
        Image(
            painter = profileImage,
            contentDescription = stringResource(R.string.profile_image),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(Constants.PROFILE_PICTURE_SIZE_LARGE)
                .aspectRatio(1f)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .clickable{
                    onProfileImageClick()
                },
        )
    }
}















