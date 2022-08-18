package com.aferrari.login.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id") val id: Int,
    @ColumnInfo(name = "user_name") val name: String?,
    @ColumnInfo(name = "user_last_name") val last_name: String?,
    @ColumnInfo(name = "user_email") val email: String?,
    @ColumnInfo(name = "user_pass") val pass: String?
)
