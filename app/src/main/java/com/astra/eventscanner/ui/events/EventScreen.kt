package com.astra.eventscanner.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astra.eventscanner.data.model.EventDto
import com.astra.eventscanner.ui.components.NeoBrutalistCard
import com.astra.eventscanner.ui.theme.Black
import com.astra.eventscanner.ui.theme.Cream
import com.astra.eventscanner.ui.theme.PrimaryPurple

@Composable
fun EventScreen(
    viewModel: EventViewModel,
    onEventSelected: (EventDto) -> Unit,
    onSettingsClick: () -> Unit
) {
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SELECT EVENT",
                color = Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
            
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .border(2.dp, Black)
                    .clickable(onClick = onSettingsClick)
                    .padding(8.dp)
            ) {
                Text(text = "⚙", fontSize = 20.sp, color = Black)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryPurple)
            }
        } else if (error != null) {
            Text(text = error!!, color = Color.Red)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(events) { event ->
                    EventCard(event = event, onClick = { onEventSelected(event) })
                }
            }
        }
    }
}

@Composable
fun EventCard(event: EventDto, onClick: () -> Unit) {
    NeoBrutalistCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = event.title.uppercase(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                color = Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.eventDate,
                fontSize = 14.sp,
                color = Black.copy(alpha = 0.7f)
            )
            Text(
                text = event.venue,
                fontSize = 14.sp,
                color = Black.copy(alpha = 0.7f)
            )
        }
    }
}
