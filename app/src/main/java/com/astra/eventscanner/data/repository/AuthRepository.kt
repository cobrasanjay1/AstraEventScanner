package com.astra.eventscanner.data.repository

import com.astra.eventscanner.data.api.ApiService
import com.astra.eventscanner.data.model.GoogleAuthRequest
import com.astra.eventscanner.data.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    val isLoggedIn: Flow<Boolean> = sessionManager.accessToken.map { it != null }
    val userName: Flow<String?> = sessionManager.userName
    val isStaff: Flow<Boolean> = sessionManager.isStaff

    suspend fun loginWithGoogle(googleIdToken: String): Result<Unit> {
        return try {
            val response = apiService.loginWithGoogle(GoogleAuthRequest(googleIdToken))
            
            if (!response.user.isStaff) {
                return Result.failure(Exception("STAFF ACCESS REQUIRED"))
            }

            sessionManager.saveSession(
                response.accessToken,
                response.refreshToken,
                response.user.name,
                response.user.email,
                response.user.isStaff
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }
}
