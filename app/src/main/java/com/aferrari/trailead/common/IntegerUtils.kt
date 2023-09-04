package com.aferrari.trailead.common

import java.security.SecureRandom
import java.util.*

class IntegerUtils {
    fun createObjectId(): Int {
        val randomId = UUID.randomUUID().mostSignificantBits.toInt()
        return if (randomId < 0) {
            randomId * (-1)
        } else {
            randomId
        }
    }

    /**
     * Return a random integer from some digits.
     * @param start is min integer number and end it is max random integer.
     */
    fun getRandomInteger(start: Int = MIN_RANDOM_INTEGER, end: Int = MAX_RANDOM_INTEGER): Int {
        require(start <= end) { "Illegal Argument" }
        val random = SecureRandom()
        random.setSeed(random.generateSeed(20))

        return random.nextInt(end - start + 1) + start
    }

    fun convertKeyAccessToInteger(first: String, second: String, third: String, four: String): Int =
        "$first$second$third$four".toInt()

    private companion object {
        private const val MIN_RANDOM_INTEGER = 0
        private const val MAX_RANDOM_INTEGER = 9999
    }
}