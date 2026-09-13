package com.astra.eventscanner.data.model

import com.google.gson.annotations.SerializedName

data class GoogleAuthRequest(
    @SerializedName("token") val token: String
)

data class AuthResponse(
    @SerializedName("refresh") val refreshToken: String,
    @SerializedName("access") val accessToken: String,
    @SerializedName("user") val user: UserDto
)

data class UserDto(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("role") val role: String,
    @SerializedName("is_staff") val isStaff: Boolean,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("college") val college: String?,
    @SerializedName("usn") val usn: String?
)
