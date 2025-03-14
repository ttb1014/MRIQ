package com.vervyle.design_system.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val ROTATION_TIME = 2000

@Composable
fun LoadingWheel(
    modifier: Modifier = Modifier,
) {
    val color = MaterialTheme.colorScheme.onSurface

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = ROTATION_TIME,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "AnimationPreview"
    )

    Canvas(
        modifier = modifier
            .padding(20.dp)
            .graphicsLayer { rotationZ = rotationAnimation * 2f }
    ) {
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = rotationAnimation,
            useCenter = false,
            style = Stroke(40f)
        )
    }
}

@Composable
fun LoadingWheelWithBorder(
    modifier: Modifier = Modifier,
) {
    LoadingWheel(
        modifier = modifier
            .border(
                2.dp,
                Color.DarkGray,
                shape = CircleShape
            )
    )
}

@Preview
@Composable
private fun WheelPreview() {
    LoadingWheel(modifier = Modifier.size(100.dp))
}

@Preview
@Composable
private fun WheelWithBorderPreview() {
    LoadingWheelWithBorder(modifier = Modifier.size(100.dp))
}