package com.aferrari.login.db

import androidx.room.Embedded
import androidx.room.Relation

data class LeaderWithTrainee(
    @Embedded val leader: Leader,
    @Relation(
        parentColumn = "leader_id",
        entityColumn = "trainee_leader_id",
        entity = Trainee::class
    )
    val trainees: List<Trainee>
)
