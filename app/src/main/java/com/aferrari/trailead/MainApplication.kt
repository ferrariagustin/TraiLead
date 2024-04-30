package com.aferrari.trailead

import android.app.Application
import com.aferrari.trailead.app.configurer.FirebaseConfigurer
import com.aferrari.trailead.app.configurer.NetworkManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseConfigurer().configure()
        NetworkManager.init(this)
    }
}