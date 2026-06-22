package com.example.uasrpl_mitradarat.ui.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        _uiState.value = DashboardUiState(
            quickTrackingList = listOf(
                QuickTrackingItem("Trans Banjarbakula", "Teman Bus Banjarmasin", Icons.Default.BusAlert),
                QuickTrackingItem("Trans Jakarta", "Koridor 1", Icons.Default.BusAlert),
                QuickTrackingItem("Trans Jogja", "Malioboro", Icons.Default.BusAlert)
            ),
            mainFeature = FeatureItem(
                id = "bus_pariwisata",
                name = "Bus Pariwisata",
                subtitle = "Cek kelayakan bus pariwisata",
                icon = Icons.Default.Shield,
                color = Color(0xFF0052CC)
            ),
            features = listOf(
                FeatureItem("teman_bus", "Teman Bus", icon = Icons.Default.DirectionsBus, color = Color(0xFFFFA000)),
                FeatureItem("brt_nusantara", "BRT Nusantara", icon = Icons.Default.BusAlert, color = Color(0xFF4CAF50)),
                FeatureItem("kspn", "KSPN", icon = Icons.Default.AirportShuttle, color = Color(0xFFE53935)),
                FeatureItem("perintis", "Perintis", icon = Icons.Default.Commute, color = Color(0xFF1E88E5)),
                FeatureItem("akap", "AKAP", icon = Icons.Default.DepartureBoard, color = Color(0xFFC0CA33)),
                FeatureItem("biskita", "BisKita", icon = Icons.Default.Group, color = Color(0xFFFB8C00))
            )
        )
    }

    fun onFeatureClick(featureId: String) {
        when (featureId) {
            "teman_bus" -> {
                // Navigasi ke Teman Bus akan ditangani di sini nanti
                println("Navigate to Teman Bus")
            }
            else -> {
                // Fitur lain belum aktif
                println("Feature $featureId is not active yet")
            }
        }
    }
}
