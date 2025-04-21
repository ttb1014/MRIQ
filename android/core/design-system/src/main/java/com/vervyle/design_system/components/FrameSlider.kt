package com.vervyle.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.R
import com.vervyle.model.Plane
import kotlin.math.roundToInt

@Composable
fun FrameSlider(
    value: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(R.drawable.slider_less),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    val newValue = (value - 1).coerceIn(0..maxValue)
                    onValueChange(newValue)
                },
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
        )
        Spacer(Modifier.width(8.dp))
        IntSlider(
            value = value,
            maxValue = maxValue,
            onValueChange = onValueChange,
            modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        Image(
            painter = painterResource(R.drawable.slider_more),
            null,
            Modifier
                .size(32.dp)
                .clickable {
                    val newValue = (value + 1).coerceIn(0..maxValue)
                    onValueChange(newValue)
                },
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
        )
    }
}

@Composable
fun IntSlider(
    value: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Slider(
        value = value.toFloat(),
        onValueChange = {
            onValueChange(it.roundToInt())
        },
        modifier = modifier,
        colors = SliderDefaults.colors(
            thumbColor = Color.LightGray,
            activeTrackColor = Color.LightGray,
            inactiveTrackColor = Color.LightGray,
        ),
        steps = 0,
        valueRange = 0f..(maxValue.toFloat() - 1),
    )
}

@Preview
@Composable
private fun IntSliderPreview() {
    var value by remember { mutableIntStateOf(12) }
    IntSlider(
        value,
        152,
        { value = it },
        Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun FrameSliderPreview() {
    var value by remember { mutableIntStateOf(0) }
    FrameSlider(
        value,
        maxValue = 152,
        { value = it },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun FrameSliderPreviewInRow() {
    var value by remember { mutableIntStateOf(0) }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Plane.entries.forEachIndexed { index, plane ->
            PlaneChip(plane, {})
            Spacer(Modifier.width(8.dp))
        }
        FrameSlider(
            value,
            maxValue = 152,
            { value = it },
            modifier = Modifier,
        )
    }
}