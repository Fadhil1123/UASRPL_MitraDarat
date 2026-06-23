package com.example.uasrpl_mitradarat.ui.map

import android.widget.Toast // Tambahan import untuk memunculkan Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // Tambahan import untuk mengambil Context
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uasrpl_mitradarat.R

@Composable
fun MapTrackingScreen(
    viewModel: MapTrackingViewModel = viewModel(factory = MapTrackingViewModel.Factory)
) {
    val context = LocalContext.current // 1. Ambil konteks aplikasi untuk wadah Toast
    val uiState by viewModel.uiState.collectAsState()

    // 2. KUNCI NOTIFIKASI COOLDOWN: Selipkan fungsi pemantau ini di sini
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { pesan ->
            Toast.makeText(context, pesan, Toast.LENGTH_LONG).show()
            viewModel.clearToast() // Bersihkan pesan agar tidak muncul berulang saat recompose
        }
    }

    val isStationSelected = uiState.selectedHalte != "Pilih Stasiun"

    val mapImageResource = when (uiState.selectedHalte) {
        "Halte Taman Siring Kilometer 0" -> R.drawable.map_siring
        "Terminal Induk KM 6" -> R.drawable.map_km6
        "Terminal Gambut Barakat" -> R.drawable.map_gambut
        "Terminal Simpang Empat Banjarbaru" -> R.drawable.map_bjb
        else -> R.drawable.dummy_map
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = mapImageResource),
            contentDescription = "Background Map",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        FloatingStationSelector(
            selectedHalte = uiState.selectedHalte,
            onHalteSelected = { halte -> viewModel.onHalteSelected(halte) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 20.dp, end = 20.dp)
        )

        if (isStationSelected) {
            FloatingActionButton(
                onClick = { viewModel.onHalteSelected("Pilih Stasiun") },
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 340.dp)
                    .size(45.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            FloatingActionButton(
                onClick = { },
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 340.dp)
                    .size(45.dp)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "My Location")
            }

            BusArrivalInfoSheet(
                stationName = uiState.selectedHalte,
                busList = uiState.busArrivalList,
                onStatusClick = { index ->
                    viewModel.onStatusClick(index)
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (uiState.isDialogShown) {
            DensityInputDialog(
                onDismiss = { viewModel.onDialogDismiss() },
                onStatusSelected = { statusTerpilih ->
                    viewModel.onStatusSelected(statusTerpilih)
                }
            )
        }
    }
}