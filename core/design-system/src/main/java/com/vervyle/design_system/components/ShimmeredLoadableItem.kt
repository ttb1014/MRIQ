package com.vervyle.design_system.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.modifiers.shimmerEffect
import com.vervyle.design_system.theme.Theme

@Composable
fun ShimmeredLoadableItem(
    loadingState: LoadingState,
    contentOnLoaded: @Composable () -> Unit,
    contentOnError: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (loadingState) {
        LoadingState.Error -> contentOnError()
        LoadingState.Loading -> Box(modifier = modifier.shimmerEffect())
        LoadingState.Loaded -> contentOnLoaded()
    }
}

sealed interface LoadingState {
    data object Loading : LoadingState
    data object Error : LoadingState
    data object Loaded : LoadingState
}

@Preview
@Composable
private fun ShimmerEffectItemPreview() {
    Theme {
        ShimmeredLoadableItem(
            LoadingState.Error,
            {},
            {},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}