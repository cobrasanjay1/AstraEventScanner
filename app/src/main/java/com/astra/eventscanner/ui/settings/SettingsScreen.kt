package com.astra.eventscanner.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astra.eventscanner.ui.components.NeoBrutalistButton
import com.astra.eventscanner.ui.theme.Black
import com.astra.eventscanner.ui.theme.Cream
import com.astra.eventscanner.ui.theme.ErrorRed

@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    onChangeEvent: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(16.dp)
    ) {
        Text(
            text = "SETTINGS",
            color = Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(32.dp))

        NeoBrutalistButton(
            text = "CHANGE EVENT",
            onClick = onChangeEvent,
            containerColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        NeoBrutalistButton(
            text = "APP SETTINGS",
            onClick = { /* TODO */ },
            containerColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        NeoBrutalistButton(
            text = "HELP & SUPPORT",
            onClick = { /* TODO */ },
            containerColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        NeoBrutalistButton(
            text = "LOGOUT",
            onClick = onLogout,
            containerColor = ErrorRed,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
