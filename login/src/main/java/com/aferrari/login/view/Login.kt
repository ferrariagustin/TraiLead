package com.aferrari.login.view

import com.aferrari.login.db.User

interface Login {
    fun goHome(user: User)
}