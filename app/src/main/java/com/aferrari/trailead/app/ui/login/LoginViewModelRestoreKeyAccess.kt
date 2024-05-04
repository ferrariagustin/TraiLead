package com.aferrari.trailead.app.ui.login

import com.aferrari.trailead.app.viewmodel.login.LoginViewModel
import com.aferrari.trailead.domain.repository.UserRepository

class LoginViewModelRestoreKeyAccess(repository: UserRepository) : LoginViewModel(repository) {

}
