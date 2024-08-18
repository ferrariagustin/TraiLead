package com.aferrari.trailead.notification.model

data class Notification(
    val title: String? = null, // Título de la notificación (opcional)
    val body: String? = null, // Cuerpo de la notificación (opcional)
    val icon: String? = null, // Icono de la notificación (opcional)
    val sound: String? = null // Sonido de la notificación (opcional)
)
