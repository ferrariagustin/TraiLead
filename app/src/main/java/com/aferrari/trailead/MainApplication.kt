package com.aferrari.trailead

import android.app.Application
import com.aferrari.trailead.app.configurer.FirebaseConfigurer
import com.aferrari.trailead.app.configurer.NetworkManager
import com.aferrari.trailead.notification.NotificationManager
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseConfigurer().configure()
        NetworkManager.init(this)
    }
}