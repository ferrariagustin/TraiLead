package com.aferrari.trailead.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        NotificationManager.displayNotification(
            this,
            message.notification?.title.toString(),
            message.notification?.body.toString()
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println()
    }
}