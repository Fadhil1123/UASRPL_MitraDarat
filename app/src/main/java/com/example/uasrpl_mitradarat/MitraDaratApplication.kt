package com.example.uasrpl_mitradarat

import android.app.Application
import com.example.uasrpl_mitradarat.data.AppContainer
import com.example.uasrpl_mitradarat.data.DefaultAppContainer

class MitraDaratApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
