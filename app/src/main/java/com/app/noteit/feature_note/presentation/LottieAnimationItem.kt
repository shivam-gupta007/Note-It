package com.app.noteit.feature_note.presentation

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieAnimationItem(
    modifier: Modifier = Modifier,
    animationUrl: String,
    iterateForever: Boolean
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(animationUrl)
    )

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        iterations = if(iterateForever) LottieConstants.IterateForever else 1
    )
}