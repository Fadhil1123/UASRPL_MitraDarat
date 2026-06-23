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
import kotlinx.coroutines.Job
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
    val selectedBusIndex: Int = -1,
    val toastMessage: String? = null
)

class MapTrackingViewModel(
    private val submitCrowdReportUseCase: SubmitCrowdReportUseCase,
    private val observeBusCrowdStatusUseCase: ObserveBusCrowdStatusUseCase
) : ViewModel() {

    private val currentUserId = "user_${UUID.randomUUID().toString().take(6)}"

    private val _uiState = MutableStateFlow(MapTrackingUiState())
    val uiState: StateFlow<MapTrackingUiState> = _uiState.asStateFlow()

    private var observeBusesJob: Job? = null

    fun onHalteSelected(halte: String) {
        _uiState.update { it.copy(selectedHalte = halte) }
        loadBusArrivals(halte)
    }

    private fun loadBusArrivals(halte: String) {
        observeBusesJob?.cancel()

        val warnaHijau = Color(0xFF28A745)
        val warnaKuning = Color(0xFFFFC107)
        val warnaMerah = Color(0xFFDC3545)
        val warnaAbu = Color.Gray

        val baseBuses = when (halte) {
            "Halte Taman Siring Kilometer 0" -> listOf(
                BusArrivalItem("K1A", warnaHijau, "3", "TB IA 05", "Siring KM 0 > Kayu Tangi", "Mencari data...", warnaAbu),
                BusArrivalItem("K1B", warnaKuning, "8", "TB IIB 01", "Siring KM 0 > Sentra Antasari", "Mencari data...", warnaAbu)
            )
            "Terminal Induk KM 6" -> listOf(
                BusArrivalItem("K2", warnaHijau, "1", "TB IC 03", "Terminal Induk KM 6 > RSUD Ulin A", "Mencari data...", warnaAbu),
                BusArrivalItem("K1B", warnaKuning, "1", "TB IB 02", "Polresta > RSUD Ulin A", "Mencari data...", warnaAbu),
                BusArrivalItem("K1B", warnaMerah, "5", "TB IB 01", "Terminal Induk KM 6 > Dharma P...", "Mencari data...", warnaAbu)
            )
            "Terminal Gambut Barakat" -> listOf(
                BusArrivalItem("K4", warnaMerah, "4", "TB IIA 12", "Gambut Barakat > KM 6", "Mencari data...", warnaAbu),
                BusArrivalItem("K3", warnaHijau, "12", "TB IIIA 04", "Gambut Barakat > Handil Bakti", "Mencari data...", warnaAbu)
            )
            "Terminal Simpang Empat Banjarbaru" -> listOf(
                BusArrivalItem("K1A", warnaKuning, "6", "TB IIA 09", "Banjarbaru > Gambut Barakat", "Mencari data...", warnaAbu),
                BusArrivalItem("K1B", warnaHijau, "15", "TB IVA 02", "Banjarbaru > Martapura", "Mencari data...", warnaAbu)
            )
            else -> emptyList()
        }

        _uiState.update { it.copy(busArrivalList = baseBuses) }
        if (baseBuses.isEmpty()) return

        observeBusesJob = viewModelScope.launch {
            baseBuses.forEachIndexed { index, bus ->
                launch {
                    observeBusCrowdStatusUseCase(bus.busCode).collect { liveStatus ->
                        val statusName = when (liveStatus) {
                            CrowdStatus.LONGGAR -> "Longgar"
                            CrowdStatus.SEDANG -> "Sedang"
                            CrowdStatus.PADAT -> "Padat"
                            CrowdStatus.BELUM_ADA_DATA -> "Belum Ada Data"
                        }
                        val color = when (liveStatus) {
                            CrowdStatus.LONGGAR -> warnaHijau
                            CrowdStatus.SEDANG -> warnaKuning
                            CrowdStatus.PADAT -> warnaMerah
                            CrowdStatus.BELUM_ADA_DATA -> warnaAbu
                        }

                        _uiState.update { state ->
                            val updatedList = state.busArrivalList.toMutableList()
                            if (index < updatedList.size && updatedList[index].busCode == bus.busCode) {
                                updatedList[index] = updatedList[index].copy(
                                    statusText = statusName,
                                    statusColor = color
                                )
                            }
                            state.copy(busArrivalList = updatedList)
                        }
                    }
                }
            }
        }
    }

    fun onStatusClick(index: Int) {
        _uiState.update { it.copy(isDialogShown = true, selectedBusIndex = index) }
    }

    fun onDialogDismiss() {
        _uiState.update { it.copy(isDialogShown = false) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onStatusSelected(statusName: String) {
        val index = _uiState.value.selectedBusIndex
        if (index < 0) return

        val bus = _uiState.value.busArrivalList[index]
        val status = when (statusName) {
            "Longgar" -> CrowdStatus.LONGGAR
            "Sedang" -> CrowdStatus.SEDANG
            "Padat" -> CrowdStatus.PADAT
            else -> CrowdStatus.BELUM_ADA_DATA
        }

        viewModelScope.launch {
            val busId = bus.busCode

            try {
                val result = submitCrowdReportUseCase(
                    userId = currentUserId,
                    busId = busId,
                    status = status
                )

                if (result.isSuccess) {
                    _uiState.update {
                        it.copy(
                            isDialogShown = false,
                            toastMessage = "Laporan berhasil dikirim! Menghitung validitas data..."
                        )
                    }
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Gagal mengirim laporan"
                    _uiState.update { it.copy(isDialogShown = false, toastMessage = errorMsg) }
                }
            } catch (e: Exception) {
                val errorLog = e.localizedMessage ?: "Terjadi kesalahan koneksi database write."
                _uiState.update {
                    it.copy(
                        isDialogShown = false,
                        toastMessage = "Firebase Error: $errorLog"
                    )
                }
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