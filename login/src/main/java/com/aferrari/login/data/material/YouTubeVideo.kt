package com.aferrari.login.data.material

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aferrari.login.data.material.dao.Material

@Entity(tableName = "youtube_video_data_table")
data class YouTubeVideo(
    @PrimaryKey(autoGenerate = true)
    override val id: Int,
    override val title: String,
    override val url: String,
    override var categoryId: Int? = null,
    override val leaderMaterialId: Int
) : Material
