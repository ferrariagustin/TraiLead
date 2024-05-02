package com.aferrari.trailead.common

import android.util.Base64
import at.favre.lib.crypto.bcrypt.BCrypt
import java.math.BigInteger
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object PasswordUtil {

    fun hashPassword(password: String): String =
        BCrypt.withDefaults().hashToString(12, password.toCharArray())

    fun verifyPassword(password: String, hashedPassword: String): Boolean =
        BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified

    fun hashSHA256(text: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(text.toByteArray())
        return Base64.encodeToString(digest.digest(), Base64.DEFAULT)
    }

    fun hashSHA256Base36(text: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(text.toByteArray())
        val bytes = digest.digest()
        val bigInteger = BigInteger(1, bytes)
        return bigInteger.toString(36)
    }
}