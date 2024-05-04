package com.aferrari.trailead.common.email


class SendMessage {
//    /**
//     * Send an email from the user's mailbox to its recipient.
//     *
//     * @param fromEmailAddress - Email address to appear in the from: header
//     * @param toEmailAddress   - Email address of the recipient
//     * @return the sent message, `null` otherwise.
//     * @throws MessagingException - if a wrongly formatted address is encountered.
//     * @throws IOException        - if service account credentials file not found.
//     */
//    @Throws(MessagingException::class, IOException::class)
//    fun sendEmail(
//        fromEmailAddress: String?,
//        toEmailAddress: String?
//    ): Message? {
//        /* Load pre-authorized user credentials from the environment.
//           TODO(developer) - See https://developers.google.com/identity for
//            guides on implementing OAuth2 for your application.*/
//        val credentials: GoogleCredential = getApplicationDefault().createScoped(GMAIL_SEND)
//        val requestInitializer: HttpRequestInitializer = HttpCredentialsAdapter(credentials)
//
//        // Create the gmail API client
//        val service = Gmail.Builder(
//            NetHttpTransport(),
//            GsonFactory.getDefaultInstance(),
//            requestInitializer
//        )
//            .setApplicationName("Gmail samples")
//            .build()
//
//        // Create the email content
//        val messageSubject = "Test message"
//        val bodyText = "lorem ipsum."
//
//        // Encode as MIME message
//        val props = Properties()
//        val session: Session = Session.getDefaultInstance(props, null)
//        val email = MimeMessage(session)
//        email.setFrom(InternetAddress(fromEmailAddress))
//        email.addRecipient(
//            Message.RecipientType.TO,
//            InternetAddress(toEmailAddress)
//        )
//        email.subject = messageSubject
//        email.setText(bodyText)
//
//        // Encode and wrap the MIME message into a gmail message
//        val buffer = ByteArrayOutputStream()
//        email.writeTo(buffer)
//        val rawMessageBytes = buffer.toByteArray()
//        val encodedEmail: String = Base64.encodeBase64URLSafeString(rawMessageBytes)
//        var message = Message()
//        message.setRaw(encodedEmail)
//        try {
//            // Create send message
//            message = service.users().messages().send("me", message).execute()
//            System.out.println("Message id: " + message.getId())
//            System.out.println(message.toPrettyString())
//            return message
//        } catch (e: GoogleJsonResponseException) {
//            // TODO(developer) - handle error appropriately
//            val error = e.details
//            if (error.code == 403) {
//                System.err.println("Unable to send message: " + e.details)
//            } else {
//                throw e
//            }
//        }
//        return null
//    }
}