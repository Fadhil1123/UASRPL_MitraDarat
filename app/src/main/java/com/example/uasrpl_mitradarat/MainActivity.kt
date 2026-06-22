package com.example.uasrpl_mitradarat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Pastikan import ini sesuai dengan lokasi folder SplashScreen-mu
import com.example.uasrpl_mitradarat.ui.splashscreen.SplashScreen
import com.example.uasrpl_mitradarat.ui.dashboard.DashboardScreen
import com.example.uasrpl_mitradarat.ui.temanbus.TemanBusScreen
import com.example.uasrpl_mitradarat.ui.theme.UASRPL_MitraDaratTheme
import com.example.uasrpl_mitradarat.ui.map.MapTrackingScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UASRPL_MitraDaratTheme {
                val navController = rememberNavController()

                // 1. UBAH startDestination menjadi "splash"
                NavHost(navController = navController, startDestination = "splash") {

                    // 2. Daftarkan rute Splash Screen milikmu
                    composable("splash") {
                        SplashScreen(
                            onLoadingFinished = {
                                // Pindah ke dashboard setelah animasi loading selesai
                                navController.navigate("dashboard") {
                                    // Hancurkan splash screen dari riwayat agar user tidak bisa menekan tombol "Back" ke layar ini
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        )
                    }

                    // Rute Dashboard (Kode asli temanmu)
                    composable("dashboard") {
                        DashboardScreen(
                            onTemanBusClick = { navController.navigate("teman_bus") }
                        )
                    }

                    // Rute Teman Bus
                    composable(route = "teman_bus") {
                        TemanBusScreen(
                            onHomeClick = { navController.popBackStack() },
                            onBusClick = { navController.navigate("map_tracking") } // TAMBAHKAN BARIS INI!
                        )
                    }

                    composable("map_tracking") {
                        MapTrackingScreen()
                    }
                }
            }
        }
    }
}