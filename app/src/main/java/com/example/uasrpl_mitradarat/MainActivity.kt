package com.example.uasrpl_mitradarat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.uasrpl_mitradarat.ui.dashboard.DashboardScreen
import com.example.uasrpl_mitradarat.ui.theme.UASRPL_MitraDaratTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UASRPL_MitraDaratTheme {
                DashboardScreen()
            }
        }
    }
}
