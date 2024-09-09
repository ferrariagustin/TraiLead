package com.aferrari.trailead.notification.model

data class FcmRequest(
    val to: String,
    val notification: NotificationBody
)
