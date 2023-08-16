package com.aferrari.trailead.common.email

import android.content.Context
import android.util.Log
import android.widget.Toast
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object EmailUtils {
    suspend fun sendEmail(email: String, context: Context) = withContext(Dispatchers.IO) {
        val senderEmail = "ferrariagustin93@gmail.com"
        val senderPassword = "tini_1_num"

        val properties = System.getProperties()
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "465"
        properties["mail.smtp.ssl.enable"] = "true"
        properties["mail.smtp.auth"] = "true"

        val session = Session.getDefaultInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(senderEmail, senderPassword)
            }
        })

        val message = MimeMessage(session)
        try {
            message.setFrom(InternetAddress(senderEmail))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            message.subject = "TraiLead - Recupera tu contraseña"
            message.setText("Ingresa el siguiente código de recuperación para restaurar tu contraseña.")

            Transport.send(message)

            // Correo electrónico enviado exitosamente
        } catch (e: Exception) {
            Log.e("Error Mail", e.message.toString())
        }
    }

}