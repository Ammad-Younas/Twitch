package com.madiwist.twitch.feature_splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.madiwist.twitch.R
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val scale = remember { Animatable(0f) }
    val overshootInterpolator = remember { OvershootInterpolator(2f) }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = {
                    overshootInterpolator.getInterpolation(it)
                }
            ),
        )
        delay(Constants.SPLASH_SCREEN_DURATION.milliseconds)
    }
    LaunchedEffect(key1 = true) {
        viewModel.authenticate()
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navController.popBackStack()
                    navController.navigate(event.route)
                }
                else -> Unit
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Image(
            modifier = Modifier.scale(scale.value),
            painter = painterResource(R.drawable.app_logo),
            contentDescription = "Logo"
        )
    }
}