package com.aferrari.trailead.data.apiservices

import com.aferrari.trailead.domain.models.MailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface MailApi {
    @GET
    fun send(
        @Url url: String,            // BuildConfig.MAIL_WEBHOOK_URL
        @Query("token") token: String,
        @Query("to") to: String,     // coma-separado si hay varios
        @Query("subject") subject: String,
        @Query("html") html: String? = null,
        @Query("text") text: String? = null,
        @Query("replyTo") replyTo: String? = null,
    ): Call<MailResponse>
}