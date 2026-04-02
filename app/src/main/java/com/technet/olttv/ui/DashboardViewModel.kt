package com.technet.olttv.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technet.olttv.data.model.DashboardResponse
import com.technet.olttv.data.repository.DashboardRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = true,
    val data: DashboardResponse? = null,
    val errorMessage: String? = null,
    val lastUpdateLabel: String = ""
)

class DashboardViewModel : ViewModel() {

    private val repository = DashboardRepository()

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var refreshJob: Job? = null

    init {
        startAutoRefresh()
    }

    fun startAutoRefresh() {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            while (true) {
                loadDashboard()
                delay(30_000)
            }
        }
    }

    fun manualRefresh() {
        viewModelScope.launch {
            loadDashboard()
        }
    }

    private suspend fun loadDashboard() {
        val current = _uiState.value
        _uiState.value = current.copy(
            isLoading = current.data == null,
            errorMessage = null
        )

        try {
            val response = repository.loadDashboard()

            if (response.ok) {
                _uiState.value = DashboardUiState(
                    isLoading = false,
                    data = response,
                    errorMessage = null,
                    lastUpdateLabel = response.generatedAt
                )
            } else {
                _uiState.value = current.copy(
                    isLoading = false,
                    errorMessage = response.message ?: "Falha ao carregar dashboard."
                )
            }
        } catch (e: Exception) {
            _uiState.value = current.copy(
                isLoading = false,
                errorMessage = e.message ?: "Erro de conexão com o servidor."
            )
        }
    }
}
