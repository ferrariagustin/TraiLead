package com.aferrari.login.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_data_table")
data class Category(

    @PrimaryKey
    @ColumnInfo(name = "category_id") val id: Int = 0,
    @ColumnInfo(name = "category_name") var name: String
//    @ColumnInfo(name = "leader_category_id") var leaderCategoryId: String
)
