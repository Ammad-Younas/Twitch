package com.madiwist.twitch.presentation.edit_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.presentation.components.TwitchTextField
import com.madiwist.twitch.presentation.components.TwitchToolBar
import com.madiwist.twitch.presentation.edit_profile.components.SkillsChips
import com.madiwist.twitch.presentation.ui.theme.ExtraSpaceLarge
import com.madiwist.twitch.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.presentation.ui.theme.SpaceSmall
import com.madiwist.twitch.presentation.utils.states.TwitchTextFieldState
import com.madiwist.twitch.utils.Constants

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {

    val availableSkills = remember { listOf("JS", "C++", "Kotlin", "Python", "Android", "Jetpack Compose") }
    val selectedSkills = viewModel.selectedSkills.value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TwitchToolBar(
            navController = navController,
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(stringResource(R.string.edit_profile))
            },
            showBackArrow = true,
            navActions = {
                IconButton(
                    onClick = {

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
                bannerImage = painterResource(R.drawable.profile_banner),
                profileImage = painterResource(R.drawable.profile_image)
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
                        viewModel.setUsernameState(state = TwitchTextFieldState(text = it))
                    },
                    hint = stringResource(R.string.username),
                    error = viewModel.usernameState.value.error,
                    leadingIcon = Icons.Filled.Person
                )
                Spacer(Modifier.height(SpaceLarge))
                TwitchTextField(
                    text = viewModel.instagramState.value.text,
                    onValueChange = {
                        viewModel.setInstagramState(state = TwitchTextFieldState(text = it))
                    },
                    hint = stringResource(R.string.instagram),
                    error = viewModel.instagramState.value.error,
                    leadingIcon = ImageVector.vectorResource(
                        R.drawable.instagram,
                    )
                )
                Spacer(Modifier.height(SpaceLarge))
                TwitchTextField(
                    text = viewModel.linkedinState.value.text,
                    onValueChange = {
                        viewModel.setLinkedinState(state = TwitchTextFieldState(text = it))
                    },
                    hint = stringResource(R.string.linkedin),
                    error = viewModel.linkedinState.value.error,
                    leadingIcon = ImageVector.vectorResource(R.drawable.linkedin)
                )
                Spacer(Modifier.height(SpaceLarge))
                TwitchTextField(
                    text = viewModel.githubState.value.text,
                    onValueChange = {
                        viewModel.setGithubState(state = TwitchTextFieldState(text = it))
                    },
                    hint = stringResource(R.string.github),
                    error = viewModel.githubState.value.error,
                    leadingIcon = ImageVector.vectorResource(R.drawable.github)
                )
                Spacer(Modifier.height(SpaceLarge))
                TwitchTextField(
                    text = viewModel.bioState.value.text,
                    onValueChange = {
                        viewModel.setBioState(state = TwitchTextFieldState(text = it))
                    },
                    hint = stringResource(R.string.bio),
                    error = viewModel.bioState.value.error,
                    leadingIcon = Icons.Default.Description,
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
                    availableSkills.forEach { skill ->
                        val isSelected = selectedSkills.contains(skill)
                        SkillsChips(
                            text = skill,
                            selected = isSelected,
                            enabled = isSelected || selectedSkills.size < 3,
                            onSelectedChange = {
                                viewModel.toggleSkillSelection(skill)
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
                .height(bannerHeight),
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
                ),
        )
    }
}















