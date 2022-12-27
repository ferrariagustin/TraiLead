package com.aferrari.login.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "material_data_table", indices = [Index(
        value = ["material_url"],
        unique = true
    )]
)
data class Material(
    @PrimaryKey
    @ColumnInfo(name = "material_id") val id: Int,
    @ColumnInfo(name = "material_title") val title: String,
    @ColumnInfo(name = "material_url") val url: String,
    @ColumnInfo(name = "category_material_id") val categoryId: Int? = null
)
