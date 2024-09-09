package com.aferrari.trailead.notification.api

import com.aferrari.trailead.data.db.NetworkModule.SERVER_KEY
import com.aferrari.trailead.notification.model.FcmResponse
import com.aferrari.trailead.notification.model.SendMessageDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmAPI {

    @Headers("Content-Type: application/json", "Authorization: Bearer $SERVER_KEY")
    @POST("/v1/projects/trailead/messages:send")
    fun sendMessage(@Body message: SendMessageDto?): Call<FcmResponse?>

    @POST("/send")
    fun send(@Body message: SendMessageDto): Call<FcmResponse?>

}
