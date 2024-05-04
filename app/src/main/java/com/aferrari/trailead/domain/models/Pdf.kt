package com.aferrari.trailead.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pdf_data_table")
data class Pdf(
    @PrimaryKey(autoGenerate = true)
    override val id: Int,
    override val title: String,
    override val url: String,
    override var categoryId: Int?,
    override val leaderMaterialId: String,
    val path: String
) : Material
