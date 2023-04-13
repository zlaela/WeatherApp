package com.example.weatherapp.ui.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.ui.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun AppSplashScreen(navController: NavController) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(1f, animationSpec =
        tween(durationMillis = 1300, easing = { easingFloat ->
            OvershootInterpolator(5f).getInterpolation(easingFloat)
        }))
        delay(1100L)
        navController.popBackStack()
        navController.navigate(route = Screens.Home.route)
    }

    Surface(modifier = Modifier.testTag("Splash")) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(colors.primary)
            .graphicsLayer {
                this.scaleX = scale.value
                this.scaleY = scale.value
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.content_desc_splash_screen)
            )
        }
    }
}
