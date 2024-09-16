package com.aferrari.trailead.common.email

import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MailAPI(
    private val email: String,
    private val subject: String,
    private val message: String
) {
    private var session: Session? = null
    suspend fun sendMessage() = withContext(Dispatchers.IO) {
        val properties = Properties()
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.socketFactory.port"] = "465"
        properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.port"] = "465"
        session = Session.getDefaultInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(EMAIL, PASSWORD)
            }
        })
        val mimeMessage = MimeMessage(session)
        try {
            mimeMessage.setFrom(InternetAddress(EMAIL))
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress(email).toString())
            mimeMessage.subject = subject
            mimeMessage.setText(message)
            Transport.send(mimeMessage)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val EMAIL = "trailead.ar@gmail.com"
        private const val PASSWORD = "coqsbjdugwlxijiy"
    }
}