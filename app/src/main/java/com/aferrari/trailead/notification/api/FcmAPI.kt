package com.aferrari.trailead.notification.api

import com.aferrari.trailead.data.db.NetworkModule.SERVER_KEY
import com.aferrari.trailead.notification.model.FcmRequest
import com.aferrari.trailead.notification.model.FcmResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmAPI {

    @Headers("Content-Type: application/json")
    @POST("/v1/projects/trailead/messages:send")
    fun sendMessage(@Body requestBody: FcmRequest?, @Header("Authorization") token: String): Call<FcmResponse?>

}
