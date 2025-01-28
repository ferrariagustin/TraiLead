package com.aferrari.trailead.common

import com.aferrari.trailead.common.common_enum.UserType
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object StringUtils {

    const val JOIN_DEEPLINK = "://"
    const val LEADER_KEY = "leader_key"
    const val TRAINEE_KEY = "trainee_key"
    const val TAB_ID = "tab_id"
    const val TRAINEE_INSTANCE_KEY = "trainee_instance_key"
    const val ERROR_WEBVIEW_PATH = "file:///android_asset/error.html"

    const val DEEPLINK_HOME = "trailead://home"
    const val DEEPLINK_LOGIN = "trailead://login"
    const val USER_NAME_KEY = "user_name"
    const val USER_EMAIL_KEY = "user_email"
    const val USER_KEY = "user_key"
    const val EMPTY_STRING = ""
    const val PDF_KEY = "pdf_key"
    const val DOWNLOAD_PDF = "download_pdf"

    fun getUserType(userType: String?): UserType? {
        when (userType) {
            UserType.LEADER.name -> UserType.LEADER
            UserType.TRAINEE.name -> UserType.TRAINEE
        }
        return null
    }

    fun isValidEmail(email: String?): Boolean {
        return !email.isNullOrEmpty()
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun getLocalDataTimeNow(format: String = "yyyy-MM-dd['T'HH:mm]"): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(format)
        return now.format(formatter)
    }

    fun getLastConnection(lastConnectionString: String): String {
        // Definir el formato de entrada (ajustar según sea necesario)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm]")

        // Convertir la cadena de entrada a un objeto LocalDateTime
        val lastConnection = LocalDateTime.parse(lastConnectionString, formatter)

        // Obtener la hora actual en UTC (ajustar la zona horaria si es necesario)
        val currentDateTime = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"))

        // Calcular la diferencia en segundos
        val duration = java.time.Duration.between(lastConnection, currentDateTime)
        val totalSeconds = duration.seconds

        // Convertir a días, horas y minutos (ajustar la lógica según tus necesidades)
        val days = totalSeconds / 86400
        val hours = (totalSeconds % 86400) / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = (totalSeconds % 60)

        // Construir el mensaje (personalizar según tus requisitos)
        return if (days > 0) {
            "Última conexión hace $days días, $hours horas y $minutes minutos."
        } else if (hours > 0) {
            "Última conexión hace $hours horas y $minutes minutos."
        } else {
            "Última conexión hace $minutes minutos y $seconds segundos."
        }
    }
}
