package com.aferrari.login.utils

import java.util.*

class IntegerUtils {
    fun getUserId(): Int {
        val randomId = UUID.randomUUID().mostSignificantBits.toInt()
        return if (randomId < 0) {
            randomId * (-1)
        } else {
            randomId
        }
    }
}