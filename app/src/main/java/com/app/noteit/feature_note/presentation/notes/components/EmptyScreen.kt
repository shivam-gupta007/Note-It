package com.app.noteit.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun EmptyScreen(message: String, animationUrl: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FunnyLottieAnimations(
            animationUrl = animationUrl
        )

        Spacer(modifier = Modifier.height(height = 5.dp))

        Text(
            text = message,
            fontSize = MaterialTheme.typography.h6.fontSize,
            color = MaterialTheme.colors.onSecondary
        )
    }
}

@Composable
fun FunnyLottieAnimations(
    animationUrl: String
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(animationUrl)
    )

    LottieAnimation(
        modifier = Modifier.size(size = 300.dp),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}