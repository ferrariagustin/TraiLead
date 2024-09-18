package com.aferrari.trailead.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.UserType

@Entity(
    tableName = "trainee_data_table", indices = [Index(
        value = ["trainee_email"],
        unique = true
    )]
)
data class Trainee(
    @PrimaryKey
    @ColumnInfo(name = "trainee_id") override val userId: String,
    @ColumnInfo(name = "trainee_name") override val name: String,
    @ColumnInfo(name = "trainee_last_name") override val lastName: String,
    @ColumnInfo(name = "trainee_email") override val email: String,
    @ColumnInfo(name = "trainee_leader_id") val leaderId: String? = null,
    val position: Position = Position.JUNIOR,
    var lastConnection: String = "",
    override val userType: UserType = UserType.TRAINEE
) : User
