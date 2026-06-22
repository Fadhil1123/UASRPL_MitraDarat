package com.example.uasrpl_mitradarat.ui.temanbus

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TemanBusViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TemanBusUiState())
    val uiState: StateFlow<TemanBusUiState> = _uiState.asStateFlow()

    init {
        loadTemanBusData()
    }

    private fun loadTemanBusData() {
        _uiState.value = TemanBusUiState(
            busList = listOf(
                TemanBusItem(
                    "1", "Kota Surakarta", "Batik Solo Trans", 10,
                    Brush.verticalGradient(listOf(Color(0xFFFFE0B2), Color(0xFFFFFFFF)))
                ),
                TemanBusItem(
                    "2", "Kota Bandung", "Metro Jabar Trans", 8,
                    Brush.verticalGradient(listOf(Color(0xFFE8F5E9), Color(0xFFFFFFFF)))
                ),
                TemanBusItem(
                    "3", "Kota Banjarmasin", "Trans Banjarbakula", 5,
                    Brush.verticalGradient(listOf(Color(0xFFF1F8E9), Color(0xFFFFFFFF)))
                ),
                TemanBusItem(
                    "4", "Kota Balikpapan", "Balikpapan City Trans", 3,
                    Brush.verticalGradient(listOf(Color(0xFFFCE4EC), Color(0xFFFFFFFF)))
                ),
                TemanBusItem(
                    "5", "Kota Palembang", "Trans Musi", 2,
                    Brush.verticalGradient(listOf(Color(0xFFFFF3E0), Color(0xFFFFFFFF)))
                ),
                TemanBusItem(
                    "6", "Kota Manado", "Trans Manado", 2,
                    Brush.verticalGradient(listOf(Color(0xFFE8EAF6), Color(0xFFFFFFFF)))
                )
            )
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}
