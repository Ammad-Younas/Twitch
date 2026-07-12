package com.madiwist.twitch.feature_auth.presentation.register

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.components.TwitchTextField
import com.madiwist.twitch.core.presentation.ui.theme.ExtraSpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.core.presentation.ui.theme.SpaceMedium
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_auth.presentation.util.AuthError

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val emailState = viewModel.emailState.value
    val usernameState = viewModel.usernameState.value
    val passwordState = viewModel.passwordState.value

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(SpaceLarge)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
            .systemBarsPadding()
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
                text = stringResource(R.string.sign_up_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(ExtraSpaceLarge))
            TwitchTextField(
                hint = stringResource(R.string.email_hint),
                text = emailState.text,
                onValueChange = { viewModel.onEvent(RegisterEvent.EnteredEmail(it)) },
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
                hint = stringResource(R.string.username_hint),
                text = usernameState.text,
                onValueChange = { viewModel.onEvent(RegisterEvent.EnteredUsername(it)) },
                error = when(usernameState.error) {
                    is AuthError.FieldEmpty -> {
                        stringResource(R.string.field_cant_be_empty)
                    }
                    is AuthError.InputTooShort -> {
                        stringResource(R.string.input_too_short, Constants.MIN_USERNAME_LENGTH)
                    }
                    else -> ""
                },
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            TwitchTextField(
                hint = stringResource(R.string.password_hint),
                text = passwordState.text,
                onValueChange = { viewModel.onEvent(RegisterEvent.EnteredPassword(it)) },
                keyboardType = KeyboardType.Password,
                showPasswordToggle = passwordState.isPasswordVisible,


                onPasswordToggleCLick = {
                    viewModel.onEvent(RegisterEvent.TogglePasswordVisibility)
                },


                error = when(passwordState.error) {
                    is AuthError.FieldEmpty -> {
                        stringResource(R.string.field_cant_be_empty)
                    }
                    is AuthError.InputTooShort -> {
                        stringResource(R.string.input_too_short, Constants.MIN_PASSWORD_LENGTH)
                    }
                    is AuthError.InvalidPassword -> {
                        stringResource(R.string.invalid_password)
                    }
                    else -> ""
                }
            )
            Spacer(modifier = Modifier.height(ExtraSpaceLarge))
            Button(
                onClick = { viewModel.onEvent(RegisterEvent.Register) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.sign_up_title),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(SpaceMedium))

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.popBackStack()
                    },
                text = buildAnnotatedString {
                    append(stringResource(R.string.already_have_an_account))
                    append(" ")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(stringResource(R.string.login))
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}