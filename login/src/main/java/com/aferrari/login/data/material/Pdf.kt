package com.aferrari.login.data.material

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aferrari.login.data.material.dao.Material

@Entity(tableName = "pdf_data_table")
data class Pdf(
    @PrimaryKey(autoGenerate = true)
    override val id: Int,
    override val title: String,
    override val url: String,
    override var categoryId: Int?,
    override val leaderMaterialId: Int,
    val path: String
) : Material
