package com.aferrari.login.repository

import com.aferrari.login.model.User
import kotlin.random.Random

class DataSource {


    /**
     * Validate if exist user for user-pass
     */
    fun login(user: String, pass: String): User = User(Random(10).nextInt(), user, pass)

    /**
     * return user for userID
     */
    fun getUser(userId: Int): User = User(Random(10).nextInt(), "Agustin", "ferrari")
}