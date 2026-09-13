package com.astra.eventscanner.data.repository

import com.astra.eventscanner.data.api.ApiService
import com.astra.eventscanner.data.model.EventDto

class EventRepository(private val apiService: ApiService) {
    suspend fun getEvents(): Result<List<EventDto>> {
        return try {
            val response = apiService.getEvents()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEventStats(eventId: Int): Result<Map<String, Int>> {
        return try {
            val registrations = apiService.getAllRegistrations()
            val eventRegistrations = registrations.filter { it.eventId == eventId }
            
            val totalRegistered = eventRegistrations.size
            val totalScanned = eventRegistrations.count { it.isUsed || it.status == "ATTENDED" }
            val remaining = totalRegistered - totalScanned

            Result.success(mapOf(
                "total" to totalRegistered,
                "scanned" to totalScanned,
                "remaining" to remaining
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
