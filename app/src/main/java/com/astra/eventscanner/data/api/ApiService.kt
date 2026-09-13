package com.astra.eventscanner.data.api

import com.astra.eventscanner.data.model.*
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/google/")
    suspend fun loginWithGoogle(@Body request: GoogleAuthRequest): AuthResponse

    @GET("api/auth/me/")
    suspend fun getCurrentUser(): UserDto

    @GET("api/events/")
    suspend fun getEvents(): List<EventDto>

    @GET("api/verify/{token}/")
    suspend fun verifyTicket(
        @Path("token") token: String
    ): ScanResponse

    @GET("api/admin-registrations/")
    suspend fun getAllRegistrations(): List<AdminRegistrationDto>
}
