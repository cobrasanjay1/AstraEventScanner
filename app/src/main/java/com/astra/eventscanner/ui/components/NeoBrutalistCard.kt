package com.astra.eventscanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.astra.eventscanner.ui.theme.Black

@Composable
fun NeoBrutalistCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    borderWidth: Dp = 3.dp,
    shadowOffset: Dp = 6.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        // Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(Black)
        )
        // Content
        Box(
            modifier = Modifier
                .background(containerColor)
                .border(borderWidth, Black),
            content = content
        )
    }
}
