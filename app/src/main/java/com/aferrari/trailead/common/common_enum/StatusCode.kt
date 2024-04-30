package com.aferrari.trailead.common.common_enum

enum class StatusCode(val value: Long) {
    SUCCESS(200L),
    ERROR(500L),
    INTERNET_CONECTION(503L),
    NOT_FOUND(404L),
    DUPLICATE(403L),
    INIT(-1),

}

fun Long.toStatusCode(): StatusCode {
    return when (this) {
        StatusCode.SUCCESS.value -> StatusCode.SUCCESS
        StatusCode.ERROR.value -> StatusCode.ERROR
        StatusCode.NOT_FOUND.value -> StatusCode.NOT_FOUND
        StatusCode.DUPLICATE.value -> StatusCode.DUPLICATE
        else -> StatusCode.INIT
    }
}

fun Long.toStatusUpdateInformation(): StatusUpdateInformation {
    return when (this) {
        StatusCode.SUCCESS.value -> StatusUpdateInformation.SUCCESS
        StatusCode.ERROR.value -> StatusUpdateInformation.FAILED
        StatusCode.NOT_FOUND.value -> StatusUpdateInformation.FAILED
        StatusCode.DUPLICATE.value -> StatusUpdateInformation.FAILED
        StatusCode.INTERNET_CONECTION.value -> StatusUpdateInformation.INTERNET_CONECTION
        else -> StatusUpdateInformation.NONE
    }
}
