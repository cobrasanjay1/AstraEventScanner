package com.astra.eventscanner.data.model

import com.google.gson.annotations.SerializedName

data class ScanResponse(
    @SerializedName("valid") val valid: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("registrant") val registrant: RegistrantDto?
)

data class RegistrantDto(
    @SerializedName("id") val id: Int,
    @SerializedName("user") val user: Int,
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_phone") val userPhone: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("college") val college: String?,
    @SerializedName("department") val department: String?,
    @SerializedName("year_of_study") val yearOfStudy: String?,
    @SerializedName("event") val eventId: Int,
    @SerializedName("event_details") val eventDetails: ScanEventDetailsDto?,
    @SerializedName("status") val status: String, // "ATTENDED", "CANCELLED", "PENDING", etc.
    @SerializedName("is_used") val isUsed: Boolean,
    @SerializedName("team_name") val teamName: String?,
    @SerializedName("team_members") val teamMembers: String?
)

data class ScanEventDetailsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("venue") val venue: String
)

data class AdminRegistrationDto(
    @SerializedName("id") val id: Int,
    @SerializedName("event") val eventId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("is_used") val isUsed: Boolean
)
