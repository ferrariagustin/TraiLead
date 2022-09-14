package com.aferrari.login.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trainee_data_table", indices = [Index(
        value = ["trainee_email"],
        unique = true
    )]
)
data class Trainee(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trainee_id") override val id: Int,
    @ColumnInfo(name = "trainee_name") override val name: String,
    @ColumnInfo(name = "trainee_last_name") override val lastName: String,
    @ColumnInfo(name = "trainee_email") override val email: String,
    @ColumnInfo(name = "trainee_pass") override val pass: String,
    @ColumnInfo(name = "trainee_leader_id") val leaderId: Int? = null,
    val position: Position = Position.JUNIOR,
    override val userType: UserType = UserType.TRAINEE
) : User
