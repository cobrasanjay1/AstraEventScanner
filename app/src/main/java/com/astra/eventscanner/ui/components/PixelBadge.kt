package com.astra.eventscanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astra.eventscanner.ui.theme.Black

@Composable
fun PixelBadge(
    text: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.uppercase(),
        color = Black,
        fontSize = 12.sp,
        fontWeight = FontWeight.Black,
        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
        modifier = modifier
            .background(containerColor)
            .border(2.dp, Black)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
