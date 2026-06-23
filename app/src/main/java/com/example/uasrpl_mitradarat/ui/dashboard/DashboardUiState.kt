package com.example.uasrpl_mitradarat.ui.dashboard

data class DashboardUiState(
    val quickTrackingList: List<QuickTrackingItem> = emptyList(),
    val features: List<FeatureItem> = emptyList(),
    val mainFeature: FeatureItem? = null
)

data class QuickTrackingItem(
    val title: String,
    val subtitle: String,
    val iconRes: Int
)

data class FeatureItem(
    val id: String,
    val name: String,
    val subtitle: String = "",
    val iconRes: Int
)