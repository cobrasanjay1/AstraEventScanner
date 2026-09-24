package com.astra.eventscanner.data.repository

import com.astra.eventscanner.data.api.ApiService
import com.astra.eventscanner.data.model.ScanResponse

class TicketRepository(private val apiService: ApiService) {
    suspend fun verifyTicket(token: String): Result<ScanResponse> {
        return try {
            val response = apiService.verifyTicket(token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markTicketUsed(token: String): Result<ScanResponse> {
        return try {
            val response = try {
                apiService.markTicketUsed(token)
            } catch (e: Exception) {
                apiService.verifyTicket(token, markUsed = true)
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
