package com.aferrari.login.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "material_data_table")
data class Material(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "material_id") val id: Int,
    @ColumnInfo(name = "material_title") val title: String,
    @ColumnInfo(name = "material_url") val url: String,
    @ColumnInfo(name = "category_material_id") val categoryId: Int? = null,
    @ColumnInfo(name = "leader_material_id") val leaderMaterialId: Int
)
