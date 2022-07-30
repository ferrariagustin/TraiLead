package com.aferrari.login.repository

import com.aferrari.login.db.User
import kotlin.random.Random

class DataSource {

    val mockEmail = "mock_email@email.com"

    /**
     * Validate if exist user for user-pass
     */
    fun login(user: String, pass: String): User =
        User(Random(10).nextInt(), user, mockEmail, pass)

    /**
     * return user for userID
     */
    fun getUser(userId: Int): User = User(Random(10).nextInt(), "Agustin", mockEmail, "ferrari")
}