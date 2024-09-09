package com.aferrari.trailead.notification.model

data class FcmResponse(
    val messageId: String? = null,
    val success: Int? = null,
    val failure: Int? = null,
    val canonicalIds: Int? = null,
    val results: List<Result>? = null
)

data class Result(
    val messageId: String? = null,
    val error: String? = null
)