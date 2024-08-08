package com.aferrari.trailead.notification.api

import com.aferrari.trailead.notification.model.FcmRequest
import com.aferrari.trailead.notification.model.FcmResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmAPI {
    @POST("projects/trailead/messages:send")
    suspend fun sendMessage(@Body requestBody: FcmRequest): FcmResponse
}
