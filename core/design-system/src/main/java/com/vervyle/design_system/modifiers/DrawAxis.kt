package com.vervyle.design_system.modifiers

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.modifiers.DrawAxisDefaults.Companion.DEFAULT_GRADIENT_END
import com.vervyle.design_system.modifiers.DrawAxisDefaults.Companion.DEFAULT_GRADIENT_START
import com.vervyle.design_system.modifiers.DrawAxisDefaults.Companion.DEFAULT_STROKE_WIDTH
import com.vervyle.design_system.theme.Theme

class DrawAxisDefaults {
    companion object {
        val DEFAULT_GRADIENT_START = Color.Yellow
        val DEFAULT_GRADIENT_END = Color.Blue
        const val DEFAULT_STROKE_WIDTH = 4f
    }
}

fun Modifier.drawAxis(
    xStep: Int,
    xMax: Int,
    yStep: Int,
    yMax: Int,
    gradientStart: Color = DEFAULT_GRADIENT_START,
    gradientEnd: Color = DEFAULT_GRADIENT_END,
    strokeWidth: Float = DEFAULT_STROKE_WIDTH,
) = this.drawWithCache {
    val widthStep = size.width / xMax
    val heightStep = size.height / yMax
    onDrawWithContent {
        drawContent()

        val verticalLinesStart = Offset(
            x = widthStep * xStep,
            y = 0f
        )
        val verticalLinesEnd = Offset(
            x = widthStep * xStep,
            y = size.height
        )
        val horizontalLinesStart = Offset(
            x = 0f,
            y = heightStep * yStep
        )
        val horizontalLinesEnd = Offset(
            x = size.width,
            y = heightStep * yStep
        )

        drawLine(
            Brush.linearGradient(
                listOf(gradientStart, gradientEnd),
                horizontalLinesStart,
                horizontalLinesEnd
            ),
            verticalLinesStart,
            verticalLinesEnd,
            strokeWidth = strokeWidth,
        )

        drawLine(
            Brush.linearGradient(
                listOf(gradientStart, gradientEnd),
                verticalLinesStart,
                verticalLinesEnd,
            ),
            horizontalLinesStart,
            horizontalLinesEnd,
            strokeWidth = strokeWidth,
        )
    }
}

@Preview
@Composable
fun AxisModifierPreview() {
    Theme {
        val transition = rememberInfiniteTransition()
        val maxVal = 151
        val width by transition.animateFloat(
            0f,
            maxVal.toFloat(),
            animationSpec = infiniteRepeatable(
                tween(
                    1000,
                    easing = LinearEasing
                ),
                RepeatMode.Reverse
            )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(16.dp)
                .drawAxis(
                    width.toInt(),
                    maxVal,
                    width.toInt(),
                    maxVal
                )
        )
    }
}