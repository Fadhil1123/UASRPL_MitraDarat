package com.example.uasrpl_mitradarat.ui.map

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun DensityInputDialog(
    onDismiss: () -> Unit,
    onStatusSelected: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Informasi Kepadatan Bus",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                DensityOptionItem(
                    title = "Longgar",
                    description = "Kursi kosong banyak",
                    icon = Icons.Default.Person,
                    color = Color(0xFF28A745),
                    onClick = { onStatusSelected("Longgar") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DensityOptionItem(
                    title = "Sedang",
                    description = "Kursi terisi penuh",
                    icon = Icons.Default.People,
                    color = Color(0xFFFFC107),
                    onClick = { onStatusSelected("Sedang") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DensityOptionItem(
                    title = "Padat",
                    description = "Berdiri berhimpitan",
                    icon = Icons.Default.Groups,
                    color = Color(0xFFDC3545),
                    onClick = { onStatusSelected("Padat") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onDismiss) {
                    Text(text = "Tutup", fontSize = 16.sp, color = Color(0xFF0D3283), fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun DensityOptionItem(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
                Text(text = description, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}