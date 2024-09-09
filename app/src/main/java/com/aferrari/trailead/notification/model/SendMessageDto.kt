package com.aferrari.trailead.notification.model


data class SendMessageDto(
    val to: String,
    val notification: NotificationBody
)


data class NotificationBody(
    val title: String? = null,
    val body: String? = null
)
