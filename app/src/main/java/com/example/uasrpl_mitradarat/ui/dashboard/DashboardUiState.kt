package com.example.uasrpl_mitradarat.ui.dashboard

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class DashboardUiState(
    val quickTrackingList: List<QuickTrackingItem> = emptyList(),
    val features: List<FeatureItem> = emptyList(),
    val mainFeature: FeatureItem? = null
)

data class QuickTrackingItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

data class FeatureItem(
    val id: String,
    val name: String,
    val subtitle: String = "",
    val icon: ImageVector,
    val color: Color,
    val hasBadge: Boolean = false
)
