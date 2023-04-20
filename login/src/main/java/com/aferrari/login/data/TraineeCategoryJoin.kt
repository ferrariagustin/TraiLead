package com.aferrari.login.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

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
    ]
)
data class TraineeCategoryJoin(
    @ColumnInfo(name = "trainee_id") val idTrainee: Int,
    @ColumnInfo(name = "category_id") val idCategory: Int
)