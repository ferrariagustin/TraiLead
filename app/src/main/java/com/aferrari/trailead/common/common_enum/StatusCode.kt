package com.aferrari.trailead.common.common_enum

enum class StatusCode(val value: Long) {
    SUCCESS(200L),
    ERROR(500L),
    NOT_FOUND(404L),
    DUPLICATE(403L),
    INIT(-1),

}
