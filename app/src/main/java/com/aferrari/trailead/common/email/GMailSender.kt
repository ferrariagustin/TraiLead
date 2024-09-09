package com.aferrari.trailead.common.email

import android.util.Log
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.Security
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GMailSender(private val user: String, private val password: String) : Authenticator() {
    private val mailhost = "smtp.gmail.com"
    private val session: Session

    init {
        val props = Properties()
        props.setProperty("mail.transport.protocol", "smtp")
        props.setProperty("mail.host", mailhost)
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.socketFactory.fallback"] = "false"
        props.setProperty("mail.smtp.quitwait", "false")
        session = Session.getDefaultInstance(props, this)
    }

    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(user, password)
    }

    @Throws(Exception::class)
    suspend fun sendMail(subject: String?, body: String, sender: String?, recipients: String) =
        withContext(Dispatchers.IO) {
            try {
                val message = MimeMessage(session)
                val handler = DataHandler(ByteArrayDataSource(body.toByteArray(), "text/plain"))
                message.sender = InternetAddress(sender)
                message.subject = subject
                message.dataHandler = handler
                if (recipients.indexOf(',') > 0) message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipients)
                ) else message.setRecipient(Message.RecipientType.TO, InternetAddress(recipients))
                Transport.send(message)
            } catch (e: Exception) {
                Log.e("SendMail", e.message, e)
            }
        }

    inner class ByteArrayDataSource : DataSource {
        private var data: ByteArray
        private var type: String? = null

        constructor(data: ByteArray, type: String?) : super() {
            this.data = data
            this.type = type
        }

        override fun getInputStream(): InputStream = ByteArrayInputStream(data)

        override fun getOutputStream(): OutputStream = throw IOException("Not Supported")

        override fun getContentType(): String =
            type ?: "application/octet-stream"

        override fun getName(): String = "ByteArrayDataSource"
    }

    companion object {
        init {
            Security.addProvider(JSSEProvider())
        }
    }
}