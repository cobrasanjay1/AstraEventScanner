package com.astra.eventscanner.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astra.eventscanner.data.model.RegistrantDto
import com.astra.eventscanner.data.model.ScanResponse
import com.astra.eventscanner.ui.components.NeoBrutalistButton
import com.astra.eventscanner.ui.scanner.DecisionState
import com.astra.eventscanner.ui.theme.*

@Composable
fun ResultScreen(
    response: ScanResponse,
    decisionState: DecisionState = DecisionState.PENDING,
    onAllow: () -> Unit = {},
    onDeny: () -> Unit = {},
    onScanNext: () -> Unit = {}
) {
    ResultContent(
        response = response,
        decisionState = decisionState,
        onAllow = onAllow,
        onDeny = onDeny,
        onScanNext = onScanNext
    )
}

@Composable
fun ResultContent(
    response: ScanResponse,
    decisionState: DecisionState,
    onAllow: () -> Unit,
    onDeny: () -> Unit,
    onScanNext: () -> Unit
) {
    val backgroundColor = when (decisionState) {
        DecisionState.PENDING -> Color.White
        DecisionState.ALLOWED -> SuccessGreen
        DecisionState.DENIED -> ErrorRed
        DecisionState.INVALID_OR_ERROR -> when {
            response.message.contains("ALREADY USED") -> WarningYellow
            else -> ErrorRed
        }
    }

    val icon = when (decisionState) {
        DecisionState.PENDING -> "?"
        DecisionState.ALLOWED -> "✓"
        DecisionState.DENIED -> "✕"
        DecisionState.INVALID_OR_ERROR -> when {
            response.message.contains("ALREADY USED") -> "!"
            else -> "✕"
        }
    }

    val title = when (decisionState) {
        DecisionState.PENDING -> "TICKET SCANNED"
        DecisionState.ALLOWED -> "ENTRY ALLOWED"
        DecisionState.DENIED -> "ENTRY DENIED"
        DecisionState.INVALID_OR_ERROR -> when {
            response.message.contains("ALREADY USED") -> "ALREADY USED"
            response.message.contains("WRONG EVENT") -> "WRONG EVENT"
            else -> "ENTRY DENIED"
        }
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
            fontSize = 100.sp,
            fontWeight = FontWeight.Black,
            color = Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            color = Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        val registrant = response.registrant
        if (registrant != null) {
            Text(
                text = registrant.userName.uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                textAlign = TextAlign.Center
            )
            if (!registrant.college.isNullOrBlank()) {
                Text(
                    text = registrant.college,
                    fontSize = 16.sp,
                    color = Black.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
            if (!registrant.department.isNullOrBlank()) {
                Text(
                    text = registrant.department,
                    fontSize = 14.sp,
                    color = Black.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            val displayStatus = when (decisionState) {
                DecisionState.PENDING -> "STATUS: UNCHECKED"
                DecisionState.ALLOWED -> "STATUS: ATTENDED (USED)"
                DecisionState.DENIED -> "STATUS: DENIED (NOT USED)"
                DecisionState.INVALID_OR_ERROR -> "STATUS: ${registrant.status}"
            }
            Text(
                text = displayStatus,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                fontFamily = FontFamily.Monospace
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

        if (decisionState == DecisionState.PENDING) {
            // Action Buttons for Allow and Deny
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NeoBrutalistButton(
                    text = "ALLOW ✓",
                    onClick = onAllow,
                    containerColor = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                NeoBrutalistButton(
                    text = "DENY ✕",
                    onClick = onDeny,
                    containerColor = ErrorRed,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            NeoBrutalistButton(
                text = "SCAN NEXT →",
                onClick = onScanNext,
                containerColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPendingPreview() {
    ResultContent(
        response = ScanResponse(
            valid = true,
            message = "Success",
            registrant = RegistrantDto(
                id = 1,
                user = 1,
                userEmail = "test@example.com",
                userName = "John Doe",
                userPhone = "1234567890",
                phoneNumber = "1234567890",
                college = "Sample University",
                department = "Computer Science",
                yearOfStudy = "3rd",
                eventId = 1,
                eventDetails = null,
                status = "REGISTERED",
                isUsed = false,
                teamName = null,
                teamMembers = null
            )
        ),
        decisionState = DecisionState.PENDING,
        onAllow = {},
        onDeny = {},
        onScanNext = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ResultScreenAllowedPreview() {
    ResultContent(
        response = ScanResponse(
            valid = true,
            message = "Success",
            registrant = RegistrantDto(
                id = 1,
                user = 1,
                userEmail = "test@example.com",
                userName = "John Doe",
                userPhone = "1234567890",
                phoneNumber = "1234567890",
                college = "Sample University",
                department = "Computer Science",
                yearOfStudy = "3rd",
                eventId = 1,
                eventDetails = null,
                status = "ATTENDED",
                isUsed = true,
                teamName = null,
                teamMembers = null
            )
        ),
        decisionState = DecisionState.ALLOWED,
        onAllow = {},
        onDeny = {},
        onScanNext = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ResultScreenDeniedPreview() {
    ResultContent(
        response = ScanResponse(
            valid = false,
            message = "ENTRY DENIED",
            registrant = RegistrantDto(
                id = 1,
                user = 1,
                userEmail = "test@example.com",
                userName = "John Doe",
                userPhone = "1234567890",
                phoneNumber = "1234567890",
                college = "Sample University",
                department = "Computer Science",
                yearOfStudy = "3rd",
                eventId = 1,
                eventDetails = null,
                status = "DENIED",
                isUsed = false,
                teamName = null,
                teamMembers = null
            )
        ),
        decisionState = DecisionState.DENIED,
        onAllow = {},
        onDeny = {},
        onScanNext = {}
    )
}
