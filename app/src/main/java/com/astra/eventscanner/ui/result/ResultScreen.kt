package com.astra.eventscanner.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astra.eventscanner.data.model.ScanResponse
import com.astra.eventscanner.ui.components.NeoBrutalistButton
import com.astra.eventscanner.ui.theme.*

@Composable
fun ResultScreen(
    response: ScanResponse,
    onScanNext: () -> Unit
) {
    val backgroundColor = when {
        response.valid -> SuccessGreen
        response.message.contains("ALREADY USED") -> WarningYellow
        else -> ErrorRed
    }

    val icon = when {
        response.valid -> "✓"
        response.message.contains("ALREADY USED") -> "!"
        else -> "✕"
    }

    val title = when {
        response.valid -> "ENTRY ALLOWED"
        response.message.contains("ALREADY USED") -> "ALREADY USED"
        response.message.contains("WRONG EVENT") -> "WRONG EVENT"
        else -> "ENTRY DENIED"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Big Icon
        Text(
            text = icon,
            fontSize = 120.sp,
            fontWeight = FontWeight.Black,
            color = Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            color = Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        val registrant = response.registrant
        if (registrant != null) {
            Text(
                text = registrant.userName.uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = registrant.college ?: "",
                fontSize = 18.sp,
                color = Black.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "STATUS: ${registrant.status}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )
        } else {
            Text(
                text = response.message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        NeoBrutalistButton(
            text = "SCAN NEXT →",
            onClick = onScanNext,
            containerColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
