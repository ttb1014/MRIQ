package com.vervyle.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.model.Plane

@Composable
fun PlaneChip(
    plane: Plane,
    onClick: (Plane) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(32.dp)
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
            .clickable {
                onClick(plane)
            }
    ) {
        BasicText(
            text = plane.name[0].uppercase(),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Preview
@Composable
private fun PlaneChipPreview() {
    PlaneChip(Plane.AXIAL, {})
}