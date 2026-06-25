package com.example.uasrpl_mitradarat.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BusArrivalItem(
    val badgeText: String,
    val badgeColor: Color,
    val minutes: String,
    val busCode: String,
    val routeText: String,
    val statusText: String,
    val statusColor: Color
)

@Composable
fun BusArrivalInfoSheet(
    stationName: String,
    busList: List<BusArrivalItem>,
    onStatusClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainKoridor = when (stationName) {
        "Halte Taman Siring Kilometer 0" -> "K1A"
        "Terminal Induk KM 6" -> "K1B"
        "Terminal Gambut Barakat" -> "K2A"
        else -> "K2B"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D3283))
                    .padding(16.dp)
            ) {
                Text(text = "Bus arrival to bus stop", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stationName, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), maxLines = 1)
                    Surface(
                        color = Color(0xFFFFC107),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(mainKoridor, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                busList.forEachIndexed { index, bus ->
                    BusArrivalRow(
                        // KUNCI UTAMA: Ubah parameter ini menjadi bus.statusColor agar badge kiri sinkron dengan tombol kanan
                        badgeColor = bus.statusColor,
                        badgeText = bus.badgeText,
                        minutes = bus.minutes,
                        busCode = bus.busCode,
                        routeText = bus.routeText,
                        statusText = bus.statusText,
                        statusColor = bus.statusColor,
                        onStatusClick = { onStatusClick(index) }
                    )
                    if (index < busList.lastIndex) {
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@Composable
fun BusArrivalRow(
    badgeColor: Color,
    badgeText: String,
    minutes: String,
    busCode: String,
    routeText: String,
    statusText: String,
    statusColor: Color,
    onStatusClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = badgeColor,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.size(width = 45.dp, height = 50.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = badgeText, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.width(70.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = minutes, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = " Min", fontSize = 12.sp, color = Color.Black, modifier = Modifier.padding(bottom = 3.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DirectionsBus, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = busCode, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = "18:24 🕒", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            Text(text = routeText, fontSize = 11.sp, color = Color.Gray, maxLines = 1)
            Text(text = "Route destination: $statusText", fontSize = 10.sp, color = Color.Gray)
        }

        Surface(
            color = statusColor,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { onStatusClick() }
        ) {
            Text(
                text = "$statusText ➔",
                color = if (statusColor == Color(0xFFFFC107)) Color.Black else Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

fun getBusDataForStation(stationName: String): List<BusArrivalItem> {
    val warnaHijau = Color(0xFF28A745)
    val warnaKuning = Color(0xFFFFC107)
    val warnaMerah = Color(0xFFDC3545)

    return when (stationName) {
        "Halte Taman Siring Kilometer 0" -> listOf(
            BusArrivalItem("K1A", warnaHijau, "3", "TB IA 05", "Siring KM 0 > Kayu Tangi", "Longgar", warnaHijau),
            BusArrivalItem("K1B", warnaKuning, "8", "TB IIB 01", "Siring KM 0 > Sentra Antasari", "Sedang", warnaKuning)
        )
        "Terminal Induk KM 6" -> listOf(
            BusArrivalItem("K2", warnaHijau, "1", "TB IC 03", "Terminal Induk KM 6 > RSUD Ulin A", "Longgar", warnaHijau),
            BusArrivalItem("K1B", warnaKuning, "1", "TB IB 02", "Polresta > RSUD Ulin A", "Sedang", warnaKuning),
            BusArrivalItem("K1B", warnaMerah, "5", "TB IB 01", "Terminal Induk KM 6 > Dharma P...", "Padat", warnaMerah)
        )
        "Terminal Gambut Barakat" -> listOf(
            BusArrivalItem("K4", warnaMerah, "4", "TB IIA 12", "Gambut Barakat > KM 6", "Padat", warnaMerah),
            BusArrivalItem("K3", warnaHijau, "12", "TB IIIA 04", "Gambut Barakat > Handil Bakti", "Longgar", warnaHijau)
        )
        "Terminal Simpang Empat Banjarbaru" -> listOf(
            BusArrivalItem("K1A", warnaKuning, "6", "TB IIA 09", "Banjarbaru > Gambut Barakat", "Sedang", warnaKuning),
            BusArrivalItem("K1B", warnaHijau, "15", "TB IVA 02", "Banjarbaru > Martapura", "Longgar", warnaHijau)
        )
        else -> emptyList()
    }
}