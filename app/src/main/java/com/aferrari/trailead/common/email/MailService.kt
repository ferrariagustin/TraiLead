package com.aferrari.trailead.common.email

import com.aferrari.trailead.data.apiservices.MailApi
import com.aferrari.trailead.domain.models.MailResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MailService {
    private val api: MailApi by lazy {
        val client = OkHttpClient.Builder()
            .callTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .followRedirects(true)
            .build()

        Retrofit.Builder()
            .baseUrl("https://script.google.com/") // dummy, usamos @Url
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MailApi::class.java)
    }

    fun sendEmail(
        to: List<String>,
        subject: String,
        html: String? = null,
        text: String? = null,
        replyTo: String? = null,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val toParam = to.joinToString(",")
        api.send(
            url = MAIL_WEBHOOK_URL,
            token = MAIL_WEBHOOK_TOKEN,
            to = toParam,
            subject = subject,
            html = html,
            text = text,
            replyTo = replyTo
        ).enqueue(object : retrofit2.Callback<MailResponse> {
            override fun onResponse(
                call: retrofit2.Call<MailResponse>,
                response: retrofit2.Response<MailResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body?.ok == true) onSuccess()
                else onError(IllegalStateException(body?.error ?: "HTTP ${response.code()}"))
            }

            override fun onFailure(call: retrofit2.Call<MailResponse>, t: Throwable) = onError(t)
        })
    }

    const val MAIL_WEBHOOK_TOKEN: String = "coqsbjdugwlxijiy"

    // Field from default config.
    const val MAIL_WEBHOOK_URL: String =
        "https://script.google.com/macros/s/AKfycbzH_Z4vAKAqauFW3VjfxlX4X2hxYnBN84H9xR453nZ5q9_7sZpOeyq1nPJFn8N0v1g/exec"
}