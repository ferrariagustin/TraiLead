package com.aferrari.trailead.domain.models

import com.aferrari.trailead.common.common_enum.UserType
import java.io.Serializable

// TODO: Pensar en como no duplicar información de leader y trainee para poder
// hacer una sola consulta cuando querramos saber si el user existe
interface User : Serializable {
    val userId: String
    val name: String
    val lastName: String
    val email: String
    val userType: UserType
}
