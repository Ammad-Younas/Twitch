package com.madiwist.twitch.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.madiwist.twitch.presentation.components.StandardTextField
import com.madiwist.twitch.presentation.ui.theme.ExtraSpaceLarge
import com.madiwist.twitch.presentation.ui.theme.SpaceLarge
import com.madiwist.twitch.presentation.ui.theme.SpaceMedium

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    Box (
        modifier = Modifier.fillMaxSize().padding(SpaceLarge)
    ){
        Column(modifier = Modifier.fillMaxSize().align(Alignment.Center),
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = stringResource(R.string.login),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(ExtraSpaceLarge))
            StandardTextField (
                hint = stringResource(R.string.username_hint),
                text = viewModel.username.value,
                onValueChange = { viewModel.setUsername(it) },
                error = viewModel.usernameError.value

            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField (
                hint = stringResource(R.string.password_hint),
                text = viewModel.password.value,
                onValueChange = { viewModel.setPassword(it) },
                keyboardType = KeyboardType.Password,
                showPasswordToggle = viewModel.showPassword.value,
                onPasswordToggleCLick = {
                    viewModel.setShowPassword(it)
                },
                error = viewModel.passwordError.value
            )
            Spacer(modifier = Modifier.height(ExtraSpaceLarge))
            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                ),
            ) {
                Text(
                    stringResource(R.string.login),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.dont_have_an_account_yet))
                append(" ")
                val sigUp = stringResource(R.string.sign_up)
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(sigUp)
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(
                Alignment.BottomCenter
            )
        )
    }
}