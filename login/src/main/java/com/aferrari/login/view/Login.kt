package com.aferrari.login.view

import com.aferrari.login.model.User

interface Login {
    fun login(user: String, pass: String)
    fun goHome(user: User)
}