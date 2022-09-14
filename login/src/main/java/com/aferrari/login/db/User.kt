package com.aferrari.login.db

// TODO: Pensar en como no duplicar información de leader y trainee para poder
// hacer una sola consulta cuando querramos saber si el user existe
interface User {
    val id: Int
    val name: String
    val lastName: String
    val email: String
    val pass: String
    val userType: UserType
}
