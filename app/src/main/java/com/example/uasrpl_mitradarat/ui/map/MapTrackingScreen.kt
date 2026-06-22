package com.example.uasrpl_mitradarat.ui.map

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uasrpl_mitradarat.R

@Composable
fun MapTrackingScreen() {
    var selectedHalte by remember { mutableStateOf("Pilih Stasiun") }
    val isStationSelected = selectedHalte != "Pilih Stasiun"

    var showDialog by remember { mutableStateOf(false) }
    var clickedBusIndex by remember { mutableStateOf(-1) }

    var currentBusList by remember { mutableStateOf(listOf<BusArrivalItem>()) }

    LaunchedEffect(selectedHalte) {
        currentBusList = getBusDataForStation(selectedHalte)
    }

    val mapImageResource = when (selectedHalte) {
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
            selectedHalte = selectedHalte,
            onHalteSelected = { halte -> selectedHalte = halte },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 20.dp, end = 20.dp)
        )

        if (isStationSelected) {
            FloatingActionButton(
                onClick = { selectedHalte = "Pilih Stasiun" },
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
                stationName = selectedHalte,
                busList = currentBusList,
                onStatusClick = { index ->
                    clickedBusIndex = index
                    showDialog = true
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (showDialog) {
            DensityInputDialog(
                onDismiss = { showDialog = false },
                onStatusSelected = { statusTerpilih ->
                    val warnaBaru = when (statusTerpilih) {
                        "Longgar" -> Color(0xFF28A745)
                        "Sedang" -> Color(0xFFFFC107)
                        else -> Color(0xFFDC3545)
                    }

                    currentBusList = currentBusList.mapIndexed { index, item ->
                        if (index == clickedBusIndex) {
                            item.copy(
                                statusText = statusTerpilih,
                                statusColor = warnaBaru
                            )
                        } else {
                            item
                        }
                    }
                    showDialog = false
                }
            )
        }
    }
}