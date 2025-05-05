package com.vervyle.design_system.components

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.theme.Theme
import kotlin.math.roundToInt

class SunAndMoonSwitchDefaults {
    class CloudPosition(
        val center: Offset,
        val radius: Dp,
        val color: Color
    )

    companion object {
        val DefaultWidth = 63.dp
        val DefaultHeight = 26.dp
        val DefaultThumbRadius = 10.dp
        val DefaultFrontCloudColor = Color(0xFFEFFEFF)
        val DefaultBackCloudColor = Color(0xFFEFFEFF).copy(alpha = 0.6f)
        val clouds: List<CloudPosition> = listOf(
            CloudPosition(
                center = Offset(
                    0f, 0f
                ),
                radius = 10.dp,
                color = DefaultFrontCloudColor
            ),
            CloudPosition(
                center = Offset(0f, 0f),
                radius = 10.dp,
                color = DefaultBackCloudColor
            ),
            CloudPosition(
                center = Offset(0f, 0f),
                radius = 10.dp,
                color = DefaultBackCloudColor
            )
        )
    }
}

@Composable
fun SunAndMoonSwitch(
    modifier: Modifier = Modifier,
    width: Dp = SunAndMoonSwitchDefaults.DefaultWidth,
    height: Dp = SunAndMoonSwitchDefaults.DefaultHeight,
    thumbRadius: Dp = SunAndMoonSwitchDefaults.DefaultThumbRadius,
) {
    val padding = (height - thumbRadius * 2) / 2
    val dropShadowColorUp = Color(0, 0, 0, (255 * 0.25).roundToInt())
    val dropShadowColorDown = Color(255, 255, 255, (255 * 0.6).roundToInt())
    val tripDistance = width - (padding + thumbRadius) * 2

    assert(thumbRadius * 2 <= height)

    val density = LocalDensity.current
    val transition = rememberInfiniteTransition()
    val offsetX by transition.animateFloat(
        initialValue = 0f,
        targetValue = with(density) { tripDistance.toPx() },
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse,
        )
    )
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.TopStart,
    ) {
        // sky
        Box(
            modifier = Modifier
                .size(width, height),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width, height)
                    .innerShadow(
                        RoundedCornerShape(200.dp),
                        dropShadowColorUp.copy(alpha = 0.5f),
                    )
                    .innerShadow(
                        shape = RoundedCornerShape(200.dp),
                        color = dropShadowColorUp.copy(alpha = 0.5f),
                        blur = 9.dp,
                        offsetY = (-4).dp,
                        offsetX = (-4).dp,
                    )
                    .dropShadow(
                        shape = RoundedCornerShape(200.dp),
                        color = dropShadowColorDown,
                        blur = 3.dp,
                        offsetY = 4.dp,
                        offsetX = 0.dp,
                        spread = 0.dp
                    )
                    .dropShadow(
                        shape = RoundedCornerShape(200.dp),
                        color = dropShadowColorUp,
                        blur = 3.dp,
                        offsetY = (-4).dp,
                        offsetX = (-1).dp,
                        spread = 0.dp
                    )
                    .background(Color(0xFF357BB3), RoundedCornerShape(200.dp)),
            ) {
            }
        }
        // rays
        Box(
            modifier = Modifier
                .size(width, height)
                .drawWithContent {
                    val shape = RoundedCornerShape(200.dp)
                    val outline = shape.createOutline(size, layoutDirection, this)
                    val path = Path().apply {
                        addOutline(outline)
                    }
                    clipPath(path) {
                        this@drawWithContent.drawContent()
                    }
                },
        ) {
            Box(
                Modifier
                    .size(thumbRadius * 2)
                    .offset(padding, padding)
                    .rays(
                        radiusStep = 42.dp,
                        initialRadius = thumbRadius,
                        count = 3,
                        color = Color(255, 255, 255, (255 * 0.1).roundToInt()),
                    )
            )
        }
        // clouds
        Box(
            modifier = Modifier
                .size(width, height)
                .drawWithContent {
                    val shape = RoundedCornerShape(200.dp)
                    val outline = shape.createOutline(size, layoutDirection, this)
                    val path = Path().apply {
                        addOutline(outline)
                    }
                    clipPath(path) {
                        this@drawWithContent.drawContent()
                    }
                },
        ) {
            Clouds(
                Modifier.size(
                    width, height
                )
            )
        }
        // thumb
        Sun(
            radius = thumbRadius,
            modifier = Modifier
                .offset(padding + (offsetX / density.density).dp, padding)
                .innerShadow(
                    shape = RoundedCornerShape(200.dp),
                    color = dropShadowColorUp.copy(alpha = 0.6f),
                    blur = 4.dp,
                    offsetX = (-3).dp,
                    offsetY = (-5).dp,
                )
                .innerShadow(
                    shape = RoundedCornerShape(200.dp),
                    color = dropShadowColorDown.copy(alpha = 0.6f),
                    blur = 3.dp,
                    offsetX = (3).dp,
                    offsetY = (4).dp,
                )
        )
    }
}

@Composable
fun Clouds(
    modifier: Modifier = Modifier,
) {
//    Spacer(modifier
//        .drawWithCache {
//            onDrawBehind {
//                drawCircle(
//                    color = Color(0xFFEFFEFF),
//                    center = Offset(
//
//                    )
//                )
//            }
//        }
//    )
}

@Composable
fun Sun(
    radius: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier
            .size(radius * 2)
            .background(Color(0xFFF1C428), RoundedCornerShape(200.dp))
    )
}

fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    spread: Dp = 0.dp
) = this.drawBehind {
    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)
    // Create a Paint object
    val paint = Paint()
    // Apply specified color
    paint.color = color

    // Check for valid blur radius
    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            // Apply blur to the Paint
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    drawIntoCanvas { canvas ->
        // Save the canvas state
        canvas.save()
        // Translate to specified offsets
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        // Draw the shadow
        canvas.drawOutline(shadowOutline, paint)
        // Restore the canvas state
        canvas.restore()
    }
}

fun Modifier.innerShadow(
    shape: Shape,
    color: Color = Color.Black,
    blur: Dp = 3.dp,
    offsetX: Dp = 3.dp,
    offsetY: Dp = 3.dp,
    spread: Dp = 0.dp
) = this.drawWithContent {
    drawContent()
    drawIntoCanvas { canvas ->
        val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)
        val paint = Paint()
        paint.color = color

        canvas.saveLayer(size.toRect(), paint)
        canvas.drawOutline(shadowOutline, paint)

        paint.asFrameworkPaint().apply {
            this.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            if (blur.toPx() > 0) {
                maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
            }
        }

        paint.color = Color.Black

        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

fun Modifier.rays(
    radiusStep: Dp,
    initialRadius: Dp,
    count: Int,
    color: Color,
) = this.drawBehind {
    repeat(count) { index ->
        drawCircle(
            color,
            radius = initialRadius.toPx() + (radiusStep.toPx() * (index + 1)),
            center = Offset(size.width / 2, size.height / 2),
        )
    }
}

fun DrawScope.drawSunBase() {
    drawCircle(Color(0xFFF1C428))
}

@Preview
@Composable
private fun SwitchPreview() {
    Theme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFA43A3A))
                .padding(16.dp)
        ) {
            SunAndMoonSwitch(
                Modifier,
                369.dp,
                145.dp,
                60.dp,
            )
        }
    }
}