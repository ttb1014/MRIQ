package com.vervyle.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vervyle.design_system.R
import com.vervyle.design_system.theme.Theme

@Composable
fun UserInput(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    TextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        trailingIcon = @Composable {
            Image(painterResource(R.drawable.search), null)
        },
        shape = RoundedCornerShape(12.dp),
    )
}

@Preview
@Composable
private fun UserInputPreview() {
    var text by remember { mutableStateOf("") }
    Theme {
        UserInput(
            text,
            { text = it },
            Modifier.fillMaxWidth()
        )
    }
}