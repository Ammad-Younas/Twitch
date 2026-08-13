package com.madiwist.twitch.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.util.ErrorImageLoading

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
                    imageVector = Icons.Default.Image,
                    contentDescription = stringResource(R.string.banner_image),
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (errorImageLoading == ErrorImageLoading.POST_TYPE) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = stringResource(R.string.banner_image),
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (errorImageLoading == ErrorImageLoading.PROFILE_TYPE) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.profile_image),
                        modifier = Modifier.fillMaxSize(0.6f),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

