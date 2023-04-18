package com.aferrari.login.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * List categories for trainee
 */
data class TraineeWithCategory(
    @Embedded val trainee: Trainee,
    @Relation(
        parentColumn = "trainee_id",
        entityColumn = "category_id",
        associateBy = Junction(TraineeCategoryJoin::class)
    )
    val categories: List<Category>
)