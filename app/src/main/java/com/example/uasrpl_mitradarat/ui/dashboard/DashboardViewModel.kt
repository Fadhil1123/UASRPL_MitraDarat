package com.example.uasrpl_mitradarat.ui.dashboard

import androidx.lifecycle.ViewModel
import com.example.uasrpl_mitradarat.R
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
                QuickTrackingItem("Trans Banjarbakula", "Teman Bus Banjarmasin", R.drawable.teman_bus),
                QuickTrackingItem("Trans Jakarta", "Koridor 1", R.drawable.teman_bus)
            ),
            mainFeature = FeatureItem(
                id = "bus_pariwisata",
                name = "Bus Pariwisata",
                subtitle = "Cek kelayakan bus pariwisata",
                iconRes = R.drawable.dishub
            ),
            features = listOf(
                FeatureItem("teman_bus", "Teman Bus", iconRes = R.drawable.teman_bus),
                FeatureItem("brt_nusantara", "BRT Nusantara", iconRes = R.drawable.brt),
                FeatureItem("kspn", "KSPN", iconRes = R.drawable.kspn),
                FeatureItem("perintis", "Perintis", iconRes = R.drawable.perintis),
                FeatureItem("akap", "AKAP", iconRes = R.drawable.akap),
                FeatureItem("biskita", "BisKita", iconRes = R.drawable.biskita)
            )
        )
    }

    fun onFeatureClick(featureId: String) {
        // Navigasi fitur lain
    }
}