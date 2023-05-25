package com.aferrari.login.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aferrari.login.data.user.dao.User

// * Resoler los siguientes issues:
// * Se pueden crear un Leader y un Trainee con el mismo mail
// * Al buscar en la base de datos esta buscando primer en el Leader y despues en el Trainee. Como
// son tablas diferentes pueden existir 2 users con el mismo userId. Lograr seprar esto!
@Entity(
    tableName = "leader_data_table", indices = [Index(
        value = ["leader_email"],
        unique = true
    )]
)
data class Leader(
    @PrimaryKey
    @ColumnInfo(name = "leader_id") override val id: Int,
    @ColumnInfo(name = "leader_name") override val name: String,
    @ColumnInfo(name = "leader_last_name") override val lastName: String,
    @ColumnInfo(name = "leader_email") override val email: String,
    @ColumnInfo(name = "leader_pass") override val pass: String,
    override val userType: UserType = UserType.LEADER
) : User
