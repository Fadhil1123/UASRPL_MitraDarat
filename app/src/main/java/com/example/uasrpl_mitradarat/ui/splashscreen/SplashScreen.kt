package com.example.uasrpl_mitradarat.ui.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.uasrpl_mitradarat.R

@Composable
fun SplashScreen(onLoadingFinished: () -> Unit = {}) {
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val totalSteps = 100
        for (i in 0..totalSteps) {
            progress = i / totalSteps.toFloat()
            delay(20)
        }
        delay(300)
        onLoadingFinished()
    }

    // 1. Perbaikan Background (Radial Gradient menyerupai cahaya di tengah)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF2890D8), // Biru muda terang di tengah
                        Color(0xFF0A3B85)  // Biru tua pekat di pinggir
                    ),
                    center = Offset(500f, 600f),
                    radius = 1500f
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mengurangi jarak atas agar konten naik
            Spacer(modifier = Modifier.height(80.dp))

            // 2. Perbaikan Logo (Ukuran presisi 100% + Efek Glow)
            Box(
                contentAlignment = Alignment.Center
            ) {
                // LAPISAN 1: Efek Glow (Ukurannya dibesarkan & cahayanya diterangkan)
                Box(
                    modifier = Modifier
                        .size(200.dp) // Ditarik jauh lebih besar dari kotak logo
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF4FC3F7).copy(alpha = 0.6f), // Cyan terang bercahaya
                                    Color.Transparent // Memudar halus di pinggirnya
                                ),
                                radius = 300f // Memaksa sebaran cahayanya memanjang ke luar
                            )
                        )
                )

                // LAPISAN 2: Kotak Biru Utama
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF53A5F8), // Biru terang atas
                                    Color(0xFF165BCC)  // Biru tua bawah
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // LAPISAN 3: Lingkaran Putih (Diperbesar agar mepet ke ujung kotak)
                    Box(
                        modifier = Modifier
                            .size(95.dp) // Tadi 95.dp, sekarang dibesarkan!
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        // LAPISAN 4: Gambar Logo M
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo Mitra Darat",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp), // Tadi 16.dp. Semakin kecil angkanya, M-nya makin JUMBO!
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Teks Judul
            Text(
                text = "MITRA DARAT",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Teman Perjalanan Anda",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp)) // Jarak disesuaikan

            // 3. Ilustrasi Rute (Mentok Kanan Kiri & Posisi Naik)
            Image(
                painter = painterResource(id = R.drawable.rute),
                contentDescription = "Ilustrasi Jaringan Transportasi",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(1f)) // Sisa ruang didorong ke bawah

            // 4. Loading Bar (Dibuat mirip dengan border glow biru muda)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MEMUAT...",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0x33FFFFFF)) // Latar belakang bar transparan
                        .border(1.dp, Color(0xFF82B1FF), RoundedCornerShape(50)) // Garis pinggir biru muda
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(50))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF82B1FF),
                                        Color.White
                                    )
                                )
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp)) // Jarak sebelum footer
        }

        // 5. Footer (Merampingkan ruang kosong di dalamnya)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Powered by",
                    color = Color.DarkGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.dishub),
                    contentDescription = "Logo Kemenhub",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Ministry of Transportation",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Indonesia",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen()
}