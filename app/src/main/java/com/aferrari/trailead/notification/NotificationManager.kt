package com.aferrari.trailead.notification

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.aferrari.trailead.R
import com.aferrari.trailead.common.IntegerUtils

object NotificationManager {

    private const val GENERIC_CHANNEL_ID = "com.aferrari.trailead.channel1"
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

    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}