package com.example.uasrpl_mitradarat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uasrpl_mitradarat.ui.dashboard.DashboardScreen
import com.example.uasrpl_mitradarat.ui.temanbus.TemanBusScreen
import com.example.uasrpl_mitradarat.ui.theme.UASRPL_MitraDaratTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UASRPL_MitraDaratTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        DashboardScreen(
                            onTemanBusClick = { navController.navigate("teman_bus") }
                        )
                    }
                    composable("teman_bus") {
                        TemanBusScreen(
                            onHomeClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
