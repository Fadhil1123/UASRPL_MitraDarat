package com.example.uasrpl_mitradarat.ui.temanbus

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class TemanBusUiState(
    val busList: List<TemanBusItem> = emptyList(),
    val searchQuery: String = ""
)

data class TemanBusItem(
    val id: String,
    val cityName: String,
    val busName: String,
    val routeCount: Int,
    val backgroundGradient: Brush
)
