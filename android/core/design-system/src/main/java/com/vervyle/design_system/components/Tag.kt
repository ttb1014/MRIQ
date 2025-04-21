package com.vervyle.design_system.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vervyle.design_system.theme.Theme

@Composable
fun Tag(
    followed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        val containerColor = if (followed) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.5f
            )
        }
        TextButton(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.textButtonColors(
                containerColor = containerColor,
                contentColor = contentColorFor(backgroundColor = containerColor),
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.12f
                ),
            ),
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                text()
            }
        }
    }
}

@Preview
@Composable
private fun TagPreview() {
    Theme {
        Tag(
            false, {}
        ) {
            Text("test")
        }
    }
}