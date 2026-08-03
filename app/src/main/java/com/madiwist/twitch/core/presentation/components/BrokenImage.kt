package com.madiwist.twitch.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.util.ErrorImageLoading

@Preview(showBackground = true)
@Composable
fun BrokenImage(
    modifier: Modifier = Modifier,
    errorImageLoading: String = ErrorImageLoading.POST_TYPE,
) {
    Box (
        modifier = modifier
    ){
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (errorImageLoading == ErrorImageLoading.BANNER_TYPE) {
                Icon(
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = stringResource(R.string.broken_image),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(50.dp)
                )
                Text(
                    text = "Error loading ${ErrorImageLoading.BANNER_TYPE}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            if (errorImageLoading == ErrorImageLoading.POST_TYPE) {
                Icon(
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = stringResource(R.string.broken_image),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(50.dp)
                )
                Text(
                    text = "Error loading ${ErrorImageLoading.POST_TYPE}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            if (errorImageLoading == ErrorImageLoading.PROFILE_TYPE) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.broken_image),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

