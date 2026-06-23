package com.example.uasrpl_mitradarat.ui.temanbus

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.uasrpl_mitradarat.MitraDaratApplication
import com.example.uasrpl_mitradarat.domain.model.Bus
import com.example.uasrpl_mitradarat.domain.repository.BusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class TemanBusViewModel(
    private val busRepository: BusRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TemanBusUiState())
    val uiState: StateFlow<TemanBusUiState> = _uiState.asStateFlow()

    init {
        observeBuses()
    }

    private fun observeBuses() {
        busRepository.getAllBuses()
            .onEach { buses ->
                _uiState.update { it.copy(busList = buses.map { bus -> bus.toUiItem() }) }
            }
            .launchIn(viewModelScope)
    }

    private fun Bus.toUiItem(): TemanBusItem {
        // Map data from Firestore model to UI model
        // Using some deterministic gradients based on busId or name for now
        val gradient = when (busName) {
            "Batik Solo Trans" -> Brush.verticalGradient(listOf(Color(0xFFFFE0B2), Color(0xFFFFFFFF)))
            "Metro Jabar Trans" -> Brush.verticalGradient(listOf(Color(0xFFE8F5E9), Color(0xFFFFFFFF)))
            "Trans Banjarbakula" -> Brush.verticalGradient(listOf(Color(0xFFF1F8E9), Color(0xFFFFFFFF)))
            "Balikpapan City Trans" -> Brush.verticalGradient(listOf(Color(0xFFFCE4EC), Color(0xFFFFFFFF)))
            "Trans Musi" -> Brush.verticalGradient(listOf(Color(0xFFFFF3E0), Color(0xFFFFFFFF)))
            "Trans Manado" -> Brush.verticalGradient(listOf(Color(0xFFE8EAF6), Color(0xFFFFFFFF)))
            else -> Brush.verticalGradient(listOf(Color(0xFFF5F5F5), Color(0xFFFFFFFF)))
        }

        return TemanBusItem(
            id = busId,
            cityName = "Kota Info", // Should ideally be in the Bus model
            busName = busName,
            routeCount = 5, // Mock value or should be in the Bus model
            backgroundGradient = gradient
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MitraDaratApplication)
                val repository = application.container.busRepository
                TemanBusViewModel(busRepository = repository)
            }
        }
    }
}
