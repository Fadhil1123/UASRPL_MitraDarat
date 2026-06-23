package com.example.uasrpl_mitradarat

import android.app.Application
import com.example.uasrpl_mitradarat.data.AppContainer
import com.example.uasrpl_mitradarat.data.DefaultAppContainer
import com.example.uasrpl_mitradarat.data.SeedDataUtility

class MitraDaratApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        
        // Seed data for demo purposes
        SeedDataUtility.seedInitialBuses()
    }
}
