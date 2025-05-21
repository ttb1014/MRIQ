package com.vervyle.design_system.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.theme.Red30

@Composable
fun AnimatedToast(
    opacity: Float,
    iconColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .graphicsLayer { alpha = opacity }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            null,
            tint = iconColor
        )
    }
}

@Preview
@Composable
private fun AnimatedToastPreview() {
    AnimatedToast(
        1f,
        iconColor = Red30,
        Icons.Error,
        Modifier
    )
}