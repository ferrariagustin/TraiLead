package com.aferrari.trailead.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "trainee_category_join",
    primaryKeys = ["trainee_id", "category_id"],
    foreignKeys = [
        ForeignKey(
            entity = Trainee::class,
            parentColumns = ["trainee_id"],
            childColumns = ["trainee_id"]
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"]
        )
    ],
    indices = [Index(
        value = ["trainee_category_join_id"],
        unique = true
    )]
)
data class TraineeCategoryJoin(
    @ColumnInfo(name = "trainee_category_join_id", defaultValue = "0") val id: Int,
    @ColumnInfo(name = "trainee_id") val idTrainee: String,
    @ColumnInfo(name = "category_id") val idCategory: Int
)