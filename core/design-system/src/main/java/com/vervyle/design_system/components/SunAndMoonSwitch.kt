package com.vervyle.design_system.components

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.vervyle.design_system.R
import com.vervyle.design_system.modifiers.dropShadow
import com.vervyle.design_system.modifiers.innerShadow
import com.vervyle.design_system.theme.Theme

class SunAndMoon {
    data class Shadow(
        val offsetX: Dp,
        val offsetY: Dp,
        val blur: Dp,
        val spread: Dp,
        val color: Color,
    )

    data class Sky(
        val color: Color = DEFAULT_DAY_SKY_COLOR,
        val width: Dp = DEFAULT_WIDTH,
        val height: Dp = DEFAULT_HEIGHT,
        val innerShadowUp: Shadow = Shadow(
            0.dp,
            1.25.dp,
            1.6.dp,
            0.dp,
            DEFAULT_BLACK_SHADOW_COLOR
        ),
        val innerShadowUpDepth: Shadow = Shadow(
            offsetX = 0.dp,
            offsetY = 0.18.dp,
            blur = 2.32.dp,
            spread = 0.dp,
            color = DEFAULT_BLACK_SHADOW_COLOR
        ),
        val dropShadowDown: Shadow = Shadow(
            offsetX = 0.dp,
            offsetY = 0.71.dp,
            blur = 0.71.dp,
            spread = 0.dp,
            color = DEFAULT_WHITE_SHADOW_COLOR
        ),
        val dropShadowUp: Shadow = Shadow(
            offsetX = 0.dp,
            offsetY = (-0.53).dp,
            blur = 0.71.dp,
            spread = 0.dp,
            color = DEFAULT_BLACK_SHADOW_COLOR
        )
    )

    data class Thumb(
        val dropShadowDownDepth: Shadow = Shadow(
            offsetX = 0.dp,
            offsetY = 0.71.dp,
            blur = 0.71.dp,
            spread = 0.dp,
            color = DEFAULT_BLACK_SHADOW_COLOR
        ),
        val dropShadowDown: Shadow = Shadow(
            offsetX = 0.71.dp,
            offsetY = 1.25.dp,
            blur = 1.42.dp,
            spread = 0.dp,
            color = DEFAULT_BLACK_SHADOW_COLOR
        ),
        val sun: Sun = Sun(),
        val moon: Moon = Moon(),
    )

    data class Sun(
        val diameter: Dp = DEFAULT_THUMB_DIAMETER,
        val color: Color = DEFAULT_SUN_COLOR,
        val innerShadowUp: Shadow = Shadow(
            offsetX = 0.53.dp,
            offsetY = 0.71.dp,
            blur = 0.71.dp,
            spread = 0.dp,
            color = DEFAULT_WHITE_SHADOW_COLOR
        ),
        val innerShadowDown: Shadow = Shadow(
            offsetX = 0.dp,
            offsetY = (-0.89).dp,
            blur = 0.71.dp,
            spread = 0.dp,
            color = DEFAULT_BLACK_SHADOW_COLOR
        )
    )

    data class Moon(
        val moonBase: MoonBase = MoonBase(),
        val spots: List<Spot> = listOf(
            Spot(
                offsetX = 9.08.dp,
                offsetY = 3.21.dp,
                diameter = 3.03.dp
            ),
            Spot(
                offsetX = 3.74.dp,
                offsetY = 9.26.dp,
                diameter = 7.48.dp
            ),
            Spot(
                offsetX = 12.64.dp,
                offsetY = 9.26.dp,
                diameter = 4.81.dp
            )
        )
    )

    data class Spot(
        val offsetX: Dp,
        val offsetY: Dp,
        val diameter: Dp,
        val color: Color = DEFAULT_SPOT_COLOR,
        val innerShadowUp: Shadow = Shadow(
            offsetX = 0.dp,
            offsetY = 0.18.dp,
            blur = 0.71.dp,
            spread = 0.dp,
            color = DEFAULT_BLACK_SHADOW_COLOR
        ),
    )

    data class MoonBase(
        val diameter: Dp = DEFAULT_THUMB_DIAMETER,
        val color: Color = DEFAULT_MOON_COLOR,
        val innerShadowUp: Shadow = Shadow(
            offsetX = 0.53.dp,
            offsetY = 0.71.dp,
            blur = 0.71.dp,
            spread = 0.dp,
            color = DEFAULT_WHITE_SHADOW_COLOR
        ),
        val innerShadowDown: Shadow = Shadow(
            offsetX = 0.dp,
            offsetY = (-0.89).dp,
            blur = 0.71.dp,
            spread = 0.dp,
            color = DEFAULT_BLACK_SHADOW_COLOR
        )
    )

    data class Star(
        val offsetX: Dp,
        val offsetY: Dp,
        val size: Dp,
        val color: Color = DEFAULT_STAR_COLOR,
        @DrawableRes val drawableId: Int = R.drawable.star,
    )

    data class Ray(
        val diameter: Dp,
        val color: Color = DEFAULT_RAY_COLOR
    )

    data class Cloud(
        val offsetX: Dp,
        val offsetY: Dp,
        val diameterX: Dp,
        val diameterY: Dp,
        val color: Color,
    )

    companion object {
        val DEFAULT_WIDTH = 65.71.dp
        val DEFAULT_HEIGHT = 26.dp
        val DEFAULT_THUMB_DIAMETER = 21.37.dp
        val DEFAULT_RAY_DIAMETER_STEP = 14.96.dp

        val DEFAULT_DAY_SKY_COLOR = Color(0xFF337BB3)
        val DEFAULT_NIGHT_SKY_COLOR = Color(0xFF1E1D2B)
        val DEFAULT_SUN_COLOR = Color(0xFFEFC529)
        val DEFAULT_MOON_COLOR = Color(0xFFC5CACD)
        val DEFAULT_SPOT_COLOR = Color(0xFF929BAF)
        val DEFAULT_STAR_COLOR = Color.White
        val DEFAULT_RAY_COLOR = Color(0x1AFFFFFF)
        val DEFAULT_FRONT_CLOUD_COLOR = Color(0xFFEFFEFF)
        val DEFAULT_BACK_CLOUD_COLOR = Color(0x99EFFEFF)

        //TODO: alpha should be 25%
        val DEFAULT_BLACK_SHADOW_COLOR = Color(0x99000000)
        val DEFAULT_WHITE_SHADOW_COLOR = Color(0xF0FFFFFF)

        val sky = Sky()
        val sun = Sun()
        val moon = Moon()
        val stars = listOf(
            Star(
                offsetX = 34.9.dp,
                offsetY = 10.33.dp,
                size = 1.25.dp,
            ),
            Star(
                offsetX = 28.85.dp,
                offsetY = 13.dp,
                size = 1.07.dp,
            ),
            Star(
                offsetX = 32.05.dp,
                offsetY = 19.23.dp,
                size = 1.6.dp,
            ),
            Star(
                offsetX = 21.19.dp,
                offsetY = 18.16.dp,
                size = 0.53.dp,
            ),
            Star(
                offsetX = 22.97.dp,
                offsetY = 8.01.dp,
                size = 1.07.dp,
            ),
            Star(
                offsetX = 13.53.dp,
                offsetY = 15.85.dp,
                size = 0.53.dp,
            ),
            Star(
                7.84.dp,
                19.59.dp,
                1.25.dp,
            ),
            Star(
                offsetX = 5.34.dp,
                offsetY = 9.79.dp,
                size = 1.25.dp,
            ),
            Star(
                offsetX = 11.22.dp,
                offsetY = 2.85.dp,
                size = 1.78.dp,
            )
        )
        val rays: List<Ray> = List(3) { index ->
            Ray(
                diameter = DEFAULT_THUMB_DIAMETER +
                        DEFAULT_RAY_DIAMETER_STEP * (index + 1),
            )
        }
        val frontClouds: List<Cloud> = listOf(
            Cloud(
                offsetX = (-4.1).dp,
                offsetY = 18.88.dp,
                diameterX = 16.56.dp,
                diameterY = 16.56.dp,
                color = DEFAULT_FRONT_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 43.1.dp,
                offsetY = 12.82.dp,
                diameterX = 30.45.dp,
                diameterY = 28.67.dp,
                color = DEFAULT_FRONT_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 17.81.dp,
                offsetY = 18.88.dp,
                diameterX = 16.03.dp,
                diameterY = 15.49.dp,
                color = DEFAULT_FRONT_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 8.73.dp,
                offsetY = 18.88.dp,
                diameterX = 16.03.dp,
                diameterY = 15.49.dp,
                color = DEFAULT_FRONT_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 21.01.dp,
                offsetY = 17.1.dp,
                diameterX = 20.12.dp,
                diameterY = 19.23.dp,
                color = DEFAULT_FRONT_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 31.16.dp,
                offsetY = 16.56.dp,
                diameterX = 22.62.dp,
                diameterY = 21.01.dp,
                color = DEFAULT_FRONT_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 53.42.dp,
                offsetY = 2.32.dp,
                diameterX = 28.67.dp,
                diameterY = 24.4.dp,
                color = DEFAULT_FRONT_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 51.64.dp,
                offsetY = (-9.97).dp,
                diameterX = 28.14.dp,
                diameterY = 27.78.dp,
                color = DEFAULT_FRONT_CLOUD_COLOR
            )
        )
        val backClouds: List<Cloud> = listOf(
            Cloud(
                offsetX = (-5.88).dp,
                offsetY = 16.92.dp,
                diameterX = 16.56.dp,
                diameterY = 16.56.dp,
                color = DEFAULT_BACK_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 43.63.dp,
                offsetY = 4.63.dp,
                diameterX = 30.45.dp,
                diameterY = 28.67.dp,
                color = DEFAULT_BACK_CLOUD_COLOR,
            ),
            Cloud(
                offsetX = 16.03.dp,
                offsetY = 16.92.dp,
                diameterX = 16.03.dp,
                diameterY = 15.49.dp,
                color = DEFAULT_BACK_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 6.95.dp,
                offsetY = 16.92.dp,
                diameterX = 16.03.dp,
                diameterY = 15.49.dp,
                color = DEFAULT_BACK_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 19.23.dp,
                offsetY = 15.14.dp,
                diameterX = 20.12.dp,
                diameterY = 19.23.dp,
                color = DEFAULT_BACK_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 29.21.dp,
                offsetY = 11.93.dp,
                diameterX = 22.62.dp,
                diameterY = 21.01.dp,
                color = DEFAULT_BACK_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 51.64.dp,
                offsetY = 0.36.dp,
                diameterX = 28.67.dp,
                diameterY = 24.4.dp,
                color = DEFAULT_BACK_CLOUD_COLOR
            ),
            Cloud(
                offsetX = 46.66.dp,
                offsetY = -12.11.dp,
                diameterX = 28.14.dp,
                diameterY = 27.78.dp,
                color = DEFAULT_BACK_CLOUD_COLOR
            )
        )
    }
}

sealed interface SunAndMoonSwitchState {
    data object SunState : SunAndMoonSwitchState
    data class SunPressedState(val offsetX: Dp) : SunAndMoonSwitchState
    data class SunToMoonState(val offsetX: Dp) : SunAndMoonSwitchState
    data object MoonState : SunAndMoonSwitchState
    data class MoonPressedState(val offsetX: Dp) : SunAndMoonSwitchState
    data class MoonToSunState(val offsetX: Dp) : SunAndMoonSwitchState
}

@Composable
fun SunAndMoonSwitch(
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val width: Dp = SunAndMoon.DEFAULT_WIDTH
    val height: Dp = SunAndMoon.DEFAULT_HEIGHT
    val thumbRadius: Dp = SunAndMoon.DEFAULT_THUMB_DIAMETER / 2
    val padding = (height - thumbRadius * 2) / 2
    val animationDistance = width - (padding + thumbRadius) * 2

    val whiteShadowColor = SunAndMoon.DEFAULT_WHITE_SHADOW_COLOR
    val blackShadowColor = SunAndMoon.DEFAULT_BLACK_SHADOW_COLOR

    val transition = rememberInfiniteTransition()
    val thumbPosition by transition.animateFloat(
        initialValue = 0f,
        targetValue = with(density) { animationDistance.toPx() },
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse,
        )
    )

    assert(thumbRadius * 2 <= height)


}

fun Modifier.innerShadow(
    shape: Shape,
    shadow: SunAndMoon.Shadow
) = this.innerShadow(
    shape = shape,
    color = shadow.color,
    blur = shadow.blur,
    offsetX = shadow.offsetX,
    offsetY = shadow.offsetY,
    spread = shadow.spread
)

fun Modifier.dropShadow(
    shape: Shape,
    shadow: SunAndMoon.Shadow
) = this.dropShadow(
    shape = shape,
    color = shadow.color,
    blur = shadow.blur,
    offsetX = shadow.offsetX,
    offsetY = shadow.offsetY,
    spread = shadow.spread,
)

@Composable
fun Sky(
    sky: SunAndMoon.Sky,
    frontClouds: List<SunAndMoon.Cloud>,
    backClouds: List<SunAndMoon.Cloud>,
    cloudsOffsetY: Dp,
    stars: List<SunAndMoon.Star>,
    starsOffsetY: Dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(
        Modifier
            .size(
                width = sky.width,
                height = sky.height
            )
            .background(sky.color, RoundedCornerShape(200.dp))
            .clouds(
                shape = RoundedCornerShape(200.dp),
                offset = Offset(0f, cloudsOffsetY.value),
                clouds = SunAndMoon.backClouds
            )
            .clouds(
                shape = RoundedCornerShape(200.dp),
                offset = Offset(0f, cloudsOffsetY.value),
                clouds = SunAndMoon.frontClouds
            )
            .stars(
                shape = RoundedCornerShape(200.dp),
                offset = Offset(0f, starsOffsetY.value),
                context = context,
                stars = stars
            )
    )
}

@Composable
fun Sun(
    sun: SunAndMoon.Sun,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier
            .size(sun.diameter)
            .background(sun.color, CircleShape)
            .innerShadow(
                CircleShape,
                sun.innerShadowUp
            )
            .innerShadow(
                CircleShape,
                sun.innerShadowDown
            )
    )
}

@Composable
fun Moon(
    moon: SunAndMoon.Moon,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .size(moon.moonBase.diameter)
            .background(moon.moonBase.color, CircleShape)
            .innerShadow(
                CircleShape,
                moon.moonBase.innerShadowUp
            )
            .innerShadow(
                CircleShape,
                moon.moonBase.innerShadowDown
            )
    ) {
        moon.spots.forEach { spot ->
            Box(
                Modifier
                    .offset(spot.offsetX, spot.offsetY)
                    .size(spot.diameter)
                    .background(spot.color, CircleShape)
                    .innerShadow(
                        CircleShape,
                        spot.innerShadowUp
                    )
            )
        }
    }
}

@Composable
fun Thumb(
    sun: SunAndMoon.Sun,
    moon: SunAndMoon.Moon,
    moonOffsetX: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .size(sun.diameter)
            .clip(CircleShape)
    ) {
        Sun(sun)
        Moon(
            moon,
            Modifier.offset(x = moonOffsetX)
        )
    }
}

fun Modifier.rays(
    center: Offset,
    rays: List<SunAndMoon.Ray>
) = this.drawBehind {
    val paint = Paint()
    drawIntoCanvas { canvas ->
        canvas.save()
        rays.forEach { ray ->
            paint.color = ray.color
            canvas.drawCircle(
                center = center,
                radius = ray.diameter.toPx() / 2,
                paint = paint
            )
        }
        canvas.restore()
    }
}

fun Modifier.clouds(
    shape: Shape,
    offset: Offset,
    clouds: List<SunAndMoon.Cloud>
) = this.drawWithContent {
    drawContent()
    val outline = shape.createOutline(size, layoutDirection, this)
    val path = Path().apply {
        when (outline) {
            is Outline.Generic -> addPath(outline.path)
            is Outline.Rectangle -> addRect(outline.rect)
            is Outline.Rounded -> addRoundRect(outline.roundRect)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.clipPath(path)

        val cloudPath = Path()
        clouds.forEach { cloud ->
            val left = cloud.offsetX.toPx()
            val top = cloud.offsetY.toPx()
            val right = left + cloud.diameterX.toPx()
            val bottom = top + cloud.diameterY.toPx()
            cloudPath.addOval(Rect(left, top, right, bottom))
        }
        val paint = Paint().apply {
            color = clouds.firstOrNull()?.color ?: Color.Transparent
            isAntiAlias = true
        }
        canvas.drawPath(cloudPath, paint)

        canvas.restore()
    }
}

fun Modifier.stars(
    shape: Shape,
    offset: Offset,
    context: Context,
    stars: List<SunAndMoon.Star>
) = this.drawWithContent {
    drawContent()
    val outline = shape.createOutline(size, layoutDirection, this)
    val path = Path().apply {
        when (outline) {
            is Outline.Generic -> addPath(outline.path)
            is Outline.Rectangle -> addRect(outline.rect)
            is Outline.Rounded -> addRoundRect(outline.roundRect)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.clipPath(path)

        stars.forEach { star ->
            val drawable =
                ContextCompat.getDrawable(context, star.drawableId) ?: return@forEach

            val left = (star.offsetX.toPx() + offset.x).toInt()
            val top = (star.offsetY.toPx() + offset.y).toInt()
            val size = star.size.toPx().toInt()
            val right = left + size
            val bottom = top + size

            drawable.setBounds(left, top, right, bottom)

            drawable.setTint(star.color.toArgb())
            drawable.draw(canvas.nativeCanvas)
        }

        canvas.restore()
    }
}

@Preview
@Composable
private fun SkyPreview() {
    Theme {
        Sky(
            SunAndMoon.sky,
            SunAndMoon.frontClouds,
            SunAndMoon.backClouds,
            0.dp,
            SunAndMoon.stars,
            0.dp
        )
    }
}

@Preview
@Composable
private fun SunPreview() {
    Theme {
        Sun(SunAndMoon.sun)
    }
}

@Preview
@Composable
private fun MoonPreview() {
    Theme {
        Moon(SunAndMoon.moon)
    }
}

@Preview
@Composable
private fun ThumbPreview() {
    val infiniteTransition = rememberInfiniteTransition()
    val density = LocalDensity.current
    val moonOffsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = with(density) {
            SunAndMoon.sun.diameter.toPx()
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    Theme {
        Thumb(
            SunAndMoon.sun,
            SunAndMoon.moon,
            moonOffsetX = moonOffsetX.dp
        )
    }
}

@Preview
@Composable
private fun RaysPreview() {
    Theme {
        Box(
            Modifier
                .size(60.dp)
                .rays(Offset.Zero, SunAndMoon.rays)
        )
    }
}

@Preview
@Composable
private fun CloudsPreview() {
    Box(
        Modifier
            .size(
                SunAndMoon.DEFAULT_WIDTH,
                SunAndMoon.DEFAULT_HEIGHT
            )
            .background(SunAndMoon.DEFAULT_DAY_SKY_COLOR, RoundedCornerShape(200.dp))
            .clouds(
                shape = RoundedCornerShape(200.dp),
                offset = Offset.Zero,
                clouds = SunAndMoon.backClouds
            )
            .clouds(
                shape = RoundedCornerShape(200.dp),
                offset = Offset.Zero,
                clouds = SunAndMoon.frontClouds
            )
    )
}

@Preview
@Composable
private fun StarsPreview() {
    val context = LocalContext.current
    Box(
        Modifier
            .size(
                SunAndMoon.DEFAULT_WIDTH,
                SunAndMoon.DEFAULT_HEIGHT
            )
            .background(SunAndMoon.DEFAULT_NIGHT_SKY_COLOR, RoundedCornerShape(200.dp))
            .stars(
                RoundedCornerShape(200.dp),
                Offset.Zero,
                context,
                SunAndMoon.stars
            )
    )
}

@Preview
@Composable
private fun SunWithRaysPreview() {
    Theme {
        Box(
            modifier = Modifier
                .size(
                    width = SunAndMoon.DEFAULT_WIDTH,
                    height = SunAndMoon.DEFAULT_HEIGHT
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            val raysOffset = Offset(
                x = with(LocalDensity.current) {
                    (SunAndMoon.sun.diameter / 2).toPx()
                },
                y = with(LocalDensity.current) {
                    (SunAndMoon.sun.diameter / 2).toPx()
                }
            )
            val modifier = Modifier
                .offset((SunAndMoon.DEFAULT_HEIGHT - SunAndMoon.DEFAULT_THUMB_DIAMETER) / 2)
            Sun(
                SunAndMoon.sun,
                Modifier
                    .then(modifier)
                    .rays(
                        raysOffset,
                        SunAndMoon.rays
                    )
            )
        }
    }
}

@Preview
@Composable
private fun SwitchPreview() {
    Theme {
        val infiniteTransition = rememberInfiniteTransition()
        val density = LocalDensity.current

    }
}