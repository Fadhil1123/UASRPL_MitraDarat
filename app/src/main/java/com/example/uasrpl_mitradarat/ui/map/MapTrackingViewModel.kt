package com.example.uasrpl_mitradarat.ui.map

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.uasrpl_mitradarat.MitraDaratApplication
import com.example.uasrpl_mitradarat.domain.model.CrowdStatus
import com.example.uasrpl_mitradarat.domain.usecase.ObserveBusCrowdStatusUseCase
import com.example.uasrpl_mitradarat.domain.usecase.SubmitCrowdReportUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class MapTrackingUiState(
    val selectedHalte: String = "Pilih Stasiun",
    val busArrivalList: List<BusArrivalItem> = emptyList(),
    val isDialogShown: Boolean = false,
    val selectedBusIndex: Int = -1
)

class MapTrackingViewModel(
    private val submitCrowdReportUseCase: SubmitCrowdReportUseCase,
    private val observeBusCrowdStatusUseCase: ObserveBusCrowdStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapTrackingUiState())
    val uiState: StateFlow<MapTrackingUiState> = _uiState.asStateFlow()

    fun onHalteSelected(halte: String) {
        _uiState.update { it.copy(selectedHalte = halte) }
        loadBusArrivals(halte)
    }

    private fun loadBusArrivals(halte: String) {
        val warnaHijau = Color(0xFF28A745)
        val warnaKuning = Color(0xFFFFC107)
        val warnaMerah = Color(0xFFDC3545)

        val mockBuses = when (halte) {
            "Halte Taman Siring Kilometer 0" -> listOf(
                BusArrivalItem("K1A", warnaHijau, "3", "TB IA 05", "Siring KM 0 > Kayu Tangi", "Longgar", warnaHijau),
                BusArrivalItem("K1B", warnaKuning, "8", "TB IIB 01", "Siring KM 0 > Sentra Antasari", "Sedang", warnaKuning)
            )
            "Terminal Induk KM 6" -> listOf(
                BusArrivalItem("K2", warnaHijau, "1", "TB IC 03", "Terminal Induk KM 6 > RSUD Ulin A", "Longgar", warnaHijau),
                BusArrivalItem("K1B", warnaKuning, "1", "TB IB 02", "Polresta > RSUD Ulin A", "Sedang", warnaKuning),
                BusArrivalItem("K1B", warnaMerah, "5", "TB IB 01", "Terminal Induk KM 6 > Dharma P...", "Padat", warnaMerah)
            )
            "Terminal Gambut Barakat" -> listOf(
                BusArrivalItem("K4", warnaMerah, "4", "TB IIA 12", "Gambut Barakat > KM 6", "Padat", warnaMerah),
                BusArrivalItem("K3", warnaHijau, "12", "TB IIIA 04", "Gambut Barakat > Handil Bakti", "Longgar", warnaHijau)
            )
            "Terminal Simpang Empat Banjarbaru" -> listOf(
                BusArrivalItem("K1A", warnaKuning, "6", "TB IIA 09", "Banjarbaru > Gambut Barakat", "Sedang", warnaKuning),
                BusArrivalItem("K1B", warnaHijau, "15", "TB IVA 02", "Banjarbaru > Martapura", "Longgar", warnaHijau)
            )
            else -> emptyList()
        }
        _uiState.update { it.copy(busArrivalList = mockBuses) }
    }

    fun onStatusClick(index: Int) {
        _uiState.update { it.copy(isDialogShown = true, selectedBusIndex = index) }
    }

    fun onDialogDismiss() {
        _uiState.update { it.copy(isDialogShown = false) }
    }

    fun onStatusSelected(statusName: String) {
        val index = _uiState.value.selectedBusIndex
        if (index < 0) return

        val bus = _uiState.value.busArrivalList[index]
        val status = when (statusName) {
            "Longgar" -> CrowdStatus.LONGGAR
            "Sedang" -> CrowdStatus.SEDANG
            "Padat" -> CrowdStatus.PADAT
            else -> CrowdStatus.NETRAL
        }

        val color = when (statusName) {
            "Longgar" -> Color(0xFF28A745)
            "Sedang" -> Color(0xFFFFC107)
            else -> Color(0xFFDC3545)
        }

        viewModelScope.launch {
            // Simplified busId mapping for demo purposes
            val busId = bus.busCode 
            
            val result = submitCrowdReportUseCase(
                userId = "currentUser", // Should be actual userId
                busId = busId,
                status = status
            )

            if (result.isSuccess) {
                // Update Local UI (or ideally observe through the flow)
                _uiState.update { state ->
                    val newList = state.busArrivalList.toMutableList()
                    newList[index] = newList[index].copy(statusText = statusName, statusColor = color)
                    state.copy(busArrivalList = newList, isDialogShown = false)
                }
            } else {
                // Here we would ideally show a Toast/Snackbar with result.exceptionOrNull()?.message
                _uiState.update { it.copy(isDialogShown = false) }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MitraDaratApplication)
                MapTrackingViewModel(
                    submitCrowdReportUseCase = application.container.submitCrowdReportUseCase,
                    observeBusCrowdStatusUseCase = application.container.observeBusCrowdStatusUseCase
                )
            }
        }
    }
}
