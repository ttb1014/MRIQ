package com.vervyle.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.R


@Composable
fun TopAppBar(
    currentDestination: String,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painterResource(R.drawable.arrow_back),
            contentDescription = null,
            modifier = Modifier.padding(4.dp),
        )
        Text(
            text = currentDestination,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )
        Icon(
            painterResource(R.drawable.search),
            contentDescription = null,
            modifier = Modifier.padding(4.dp),
        )
    }
}


@Preview
@Composable
private fun TopAppBarPreview() {
    TopAppBar("Quiz", {}, {})
}