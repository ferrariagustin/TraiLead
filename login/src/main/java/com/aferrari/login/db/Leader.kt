package com.aferrari.login.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "leader_data_table", indices = [Index(
        value = ["leader_email"],
        unique = true
    )]
)
data class Leader(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "leader_id") override val id: Int,
    @ColumnInfo(name = "leader_name") override val name: String,
    @ColumnInfo(name = "leader_last_name") override val lastName: String,
    @ColumnInfo(name = "leader_email") override val email: String,
    @ColumnInfo(name = "leader_pass") override val pass: String,
    override val userType: UserType = UserType.LEADER
) : User
