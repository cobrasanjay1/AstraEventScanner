package com.astra.eventscanner.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astra.eventscanner.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UiEventStats(
    val scanned: Int,
    val total: Int,
    val remaining: Int
)

class StatsViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _stats = MutableStateFlow<UiEventStats?>(null)
    val stats: StateFlow<UiEventStats?> = _stats

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadStats(eventId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = eventRepository.getEventStats(eventId)
            _isLoading.value = false
            if (result.isSuccess) {
                val data = result.getOrNull()
                if (data != null) {
                    _stats.value = UiEventStats(
                        scanned = data["scanned"] ?: 0,
                        total = data["total"] ?: 0,
                        remaining = data["remaining"] ?: 0
                    )
                }
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to load stats"
            }
        }
    }
}
