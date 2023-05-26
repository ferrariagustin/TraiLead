package com.aferrari.trailead.app.ui.login

import com.aferrari.trailead.domain.models.User

interface Login {
    fun goHome(user: User)
}