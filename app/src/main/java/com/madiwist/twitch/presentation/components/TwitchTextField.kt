package com.madiwist.twitch.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.madiwist.twitch.R
import com.madiwist.twitch.presentation.ui.theme.SocialIconMedium

@Composable
fun TwitchTextField(
    text: String = "",
    hint: String = "",
    error: String = "",
    maxLength: Int = 40,
    showPasswordToggle : Boolean = false,
    onPasswordToggleCLick : (Boolean) -> Unit = {},
    leadingIcon: ImageVector? = null,
    @StringRes leadingIconDescription: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPasswordToggleDisplayed : Boolean = keyboardType == KeyboardType.Password,
    onValueChange : (String) -> Unit,
) {
    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            isError = error != "",

            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            singleLine = true,
            visualTransformation = if (!showPasswordToggle && isPasswordToggleDisplayed){
                PasswordVisualTransformation()
            }else {
                VisualTransformation.None
            },
            leadingIcon = if (leadingIcon != null) {
                @Composable {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = leadingIconDescription?.let { stringResource(it) },
                        modifier = Modifier.size(SocialIconMedium)
                    )
                }
            } else {
                null
            },
            trailingIcon = if (isPasswordToggleDisplayed) {
                @Composable {
                    IconButton(
                        onClick = {
                            onPasswordToggleCLick(!showPasswordToggle)
                        }
                    )
                    {
                        Icon(
                            imageVector = if (showPasswordToggle) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            },
                            contentDescription = if (showPasswordToggle) {
                                stringResource(R.string.password_visible_content_description)
                            } else {
                                stringResource(R.string.password_hidden_content_description)
                            },
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                null
            },
            value = text,
            onValueChange = {
                if (it.length <= maxLength){
                    onValueChange(it)
                }
            },
            placeholder = { Text(hint, style = MaterialTheme.typography.bodyLarge) },
        )
        if (error.isNotEmpty()){
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}