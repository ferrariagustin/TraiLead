package com.aferrari.login.view

import com.aferrari.login.database.User

interface Login {
    fun goHome(user: User)
}