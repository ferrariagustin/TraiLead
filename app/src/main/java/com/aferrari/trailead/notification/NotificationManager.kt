package com.aferrari.trailead.notification

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.aferrari.trailead.R
import com.aferrari.trailead.common.IntegerUtils

object NotificationManager {

    val GENERIC_CHANNEL_ID = "com.aferrari.trailead.channel1"
    private var notificationManager: NotificationManager? = null
    private const val CHANNEL_NAME: String = "trailead_channel"
    private const val CHANNEL_DESCRIPTION: String = "This channel is for generic notifications"

    fun displayNotification(
        context: Context,
        textTitle: String,
        textContent: String,
        id: String = GENERIC_CHANNEL_ID
    ) {
        val builder = NotificationCompat.Builder(context, id)
            .setSmallIcon(R.drawable.trailead_bg_white)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH).build()
        notificationManager?.notify(IntegerUtils().createObjectId(), builder)
    }


    fun createNotificationChannel(
        activity: Activity,
        id: String = GENERIC_CHANNEL_ID,
        name: String = CHANNEL_NAME,
        channelDescription: String = CHANNEL_DESCRIPTION
    ) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance).apply {
            description = channelDescription
        }
        // Register the channel with the system.
        notificationManager =
            activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }

    fun createGenericNotificationChannel(
        activity: Activity
    ) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(GENERIC_CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }
        // Register the channel with the system.
        notificationManager =
            activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }

    fun displayWelcomeNotification(
        context: Context,
        textTitle: String,
        textContent: String
    ) {

        displayNotification(context, textTitle, textContent)
    }
}