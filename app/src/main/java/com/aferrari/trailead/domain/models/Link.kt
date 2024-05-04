package com.aferrari.trailead.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "link_data_table")
data class Link(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    override val title: String,
    override val url: String,
    override var categoryId: Int?,
    override val leaderMaterialId: String
) : Material