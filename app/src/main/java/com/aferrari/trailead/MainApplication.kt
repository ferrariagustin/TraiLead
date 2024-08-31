package com.aferrari.trailead

import android.app.Application
import com.aferrari.trailead.app.configurer.FirebaseConfigurer
import com.aferrari.trailead.app.configurer.NetworkManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import java.io.FileInputStream




@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseConfigurer().configure()
        NetworkManager.init(this)
    }
}