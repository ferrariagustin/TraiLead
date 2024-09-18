package com.aferrari.trailead.common

import com.aferrari.trailead.common.common_enum.UserType
import java.time.LocalDateTime

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

    fun getLocalDataTimeNow(): String {
        val now = LocalDateTime.now()
        return "${now.hour}:${now.minute}:${now.second}"
    }

    fun getLastConnection(lastConnection: String): String {
        // Dividimos las cadenas en horas, minutos y segundos
        val partesUltimaConexion = lastConnection.split(":").map { it.toInt() }
        val partesHoraActual = getLocalDataTimeNow().split(":").map { it.toInt() }

        // Calculamos la diferencia en segundos
        val diferenciaSegundos =
            (partesHoraActual[0] * 3600 + partesHoraActual[1] * 60 + partesHoraActual[2]) -
                    (partesUltimaConexion[0] * 3600 + partesUltimaConexion[1] * 60 + partesUltimaConexion[2])

        // Convertimos la diferencia a horas, minutos y segundos
        val horas = diferenciaSegundos / 3600
        val minutos = (diferenciaSegundos % 3600) / 60
        val segundos = diferenciaSegundos % 60

        // Construimos el mensaje
        return "Última conexión hace ${getHoursToString(horas)} ${getMinutesToString(minutos)} $segundos segundos."
    }

    private fun getHoursToString(hours: Int): String {
        return if (hours > 0) "$hours horas," else ""
    }

    private fun getMinutesToString(minutes: Int): String {
        return if (minutes > 0) "$minutes minutos y" else ""
    }

}