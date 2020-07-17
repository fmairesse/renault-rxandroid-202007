package com.example.renaultrx

import android.app.Application
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Démarrer Koin avec notre configuration
        startKoin {
            modules(appModule)
        }
    }
}