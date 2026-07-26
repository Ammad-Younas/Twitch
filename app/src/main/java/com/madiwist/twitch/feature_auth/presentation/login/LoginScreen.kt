package com.madiwist.twitch.feature_auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.components.TwitchTextField
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.ui.theme.ExtraSpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.presentation.util.asString
import com.madiwist.twitch.feature_auth.presentation.util.AuthError
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    snackbarHostState: SnackbarHostState,
    onNavigate: (String) -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val emailState = viewModel.emailState.value
    val passwordState = viewModel.passwordState.value
    val loginState = viewModel.loginState.value
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context),
                        duration = SnackbarDuration.Long
                    )
                }
                is UiEvent.Navigate -> {
                    onNavigate(event.route)
                }
                is UiEvent.NavigateUp -> Unit
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
            .padding(SpaceLarge)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = maxHeight)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.login_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(ExtraSpaceLarge))
            TwitchTextField(
                hint = stringResource(R.string.email_hint),
                text = emailState.text,
                onValueChange = { viewModel.onEvent(LoginEvent.EnteredEmail(it)) },
                error = when(emailState.error) {
                    is AuthError.FieldEmpty -> {
                        stringResource(R.string.field_cant_be_empty)
                    }
                    is AuthError.InvalidEmail -> {
                        stringResource(R.string.not_a_valid_email)
                    }
                    else -> ""
                },
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            TwitchTextField(
                hint = stringResource(R.string.password_hint),
                text = passwordState.text,
                onValueChange = { viewModel.onEvent(LoginEvent.EnteredPassword(it)) },
                keyboardType = KeyboardType.Password,
                showPasswordToggle = passwordState.isPasswordVisible,
                onPasswordToggleCLick = {
                    viewModel.onEvent(LoginEvent.TogglePasswordVisibility)
                },
                error = when(passwordState.error) {
                    is AuthError.FieldEmpty -> {
                        stringResource(R.string.field_cant_be_empty)
                    }
                    else -> ""
                }
            )
            Spacer(modifier = Modifier.height(ExtraSpaceLarge))
            Button(
                onClick = {
                    viewModel.onEvent(LoginEvent.Login)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = !loginState.isLoading,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (loginState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.login_title),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpaceMedium))

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onNavigate((Screen.RegisterScreen.route))
                    },
                text = buildAnnotatedString {
                    append(stringResource(R.string.dont_have_an_account_yet))
                    append(" ")

                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(stringResource(R.string.sign_up))
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}