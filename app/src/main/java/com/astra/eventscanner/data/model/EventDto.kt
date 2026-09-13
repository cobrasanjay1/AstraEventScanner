package com.astra.eventscanner.data.model

import com.google.gson.annotations.SerializedName

data class EventDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("event_date") val eventDate: String,
    @SerializedName("venue") val venue: String,
    @SerializedName("image") val image: String?,
    @SerializedName("category") val category: String,
    @SerializedName("time") val time: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("registration_limit") val registrationLimit: Int,
    @SerializedName("is_registration_open") val isRegistrationOpen: Boolean,
    @SerializedName("requires_payment") val requiresPayment: Boolean,
    @SerializedName("payment_amount") val paymentAmount: String
)

// Extension properties to maintain UI compatibility if needed,
// though we should prefer the actual backend names.
val EventDto.name: String get() = title
val EventDto.date: String get() = eventDate
val EventDto.location: String get() = venue
