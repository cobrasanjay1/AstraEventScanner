package com.astra.eventscanner.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astra.eventscanner.data.model.ScanResponse
import com.astra.eventscanner.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScannerViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _scanResult = MutableStateFlow<ScanResponse?>(null)
    val scanResult: StateFlow<ScanResponse?> = _scanResult

    private val _isScanning = MutableStateFlow(true)
    val isScanning: StateFlow<Boolean> = _isScanning

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var lastScannedToken: String? = null
    private var isVerificationInProgress = false

    fun onQrDetected(token: String, selectedEventId: Int) {
        if (isVerificationInProgress || (token == lastScannedToken)) return

        isVerificationInProgress = true
        lastScannedToken = token
        
        viewModelScope.launch {
            _isScanning.value = false
            _isLoading.value = true
            _error.value = null
            
            val result = ticketRepository.verifyTicket(token)
            _isLoading.value = false
            isVerificationInProgress = false
            
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response != null) {
                    val finalResponse = applySafetyChecks(response, selectedEventId)
                    _scanResult.value = finalResponse
                }
            } else {
                // Handle 404 or other network errors
                val exception = result.exceptionOrNull()
                val message = if (exception is retrofit2.HttpException && exception.code() == 404) {
                    "INVALID TICKET QR CODE"
                } else {
                    "SERVER ERROR: Unable to verify ticket."
                }
                _error.value = message
                _scanResult.value = ScanResponse(
                    valid = false,
                    message = message,
                    registrant = null
                )
            }
        }
    }

    private fun applySafetyChecks(response: ScanResponse, selectedEventId: Int): ScanResponse {
        val registrant = response.registrant ?: return response

        // Safety Check 1: Wrong Event
        if (registrant.eventId != selectedEventId) {
            return response.copy(
                valid = false,
                message = "ENTRY DENIED: WRONG EVENT"
            )
        }

        // Safety Check 2: Cancelled
        if (registrant.status == "CANCELLED") {
            return response.copy(
                valid = false,
                message = "ENTRY DENIED: TICKET CANCELLED"
            )
        }

        // Safety Check 3: Pending
        if (registrant.status == "PENDING") {
            return response.copy(
                valid = false,
                message = "ENTRY DENIED: PAYMENT PENDING"
            )
        }

        // Safety Check 4: Already Used
        if (!response.valid || registrant.isUsed || registrant.status == "ATTENDED") {
            return response.copy(
                valid = false,
                message = "TICKET ALREADY USED"
            )
        }

        return response
    }

    fun resetScanner() {
        _scanResult.value = null
        _isScanning.value = true
        _error.value = null
        lastScannedToken = null
    }
}
