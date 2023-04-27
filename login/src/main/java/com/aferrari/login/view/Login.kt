package com.aferrari.login.view

import com.aferrari.login.data.user.dao.User

interface Login {
    fun goHome(user: User)
}