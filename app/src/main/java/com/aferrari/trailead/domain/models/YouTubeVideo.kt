package com.aferrari.trailead.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "youtube_video_data_table")
data class YouTubeVideo(
    @PrimaryKey(autoGenerate = true)
    override val id: Int,
    override val title: String,
    override val url: String,
    override var categoryId: Int? = null,
    override val leaderMaterialId: Int
) : Material
