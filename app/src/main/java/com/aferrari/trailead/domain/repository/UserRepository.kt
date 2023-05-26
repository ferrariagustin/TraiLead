package com.aferrari.trailead.domain.repository

import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.User

class UserRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun initLocalDataSource() {
        val leaderList = remoteDataSource.getAllLeader()
        leaderList.forEach {
            if (localDataSource.getLeader(it.email) == null) {
                localDataSource.insertLeader(it)
            }
        }
        val traineeList = remoteDataSource.getAllTrainee()
        traineeList.forEach {
            if (localDataSource.getTrainee(it.email) == null) {
                localDataSource.insertTrainee(it)
            }
        }
    }

    suspend fun get(user_id: Int): User? {
        localDataSource.getLeader(user_id).apply {
            if (this == null) {
                localDataSource.getTrainee(user_id).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun get(user_email: String): User? {
        localDataSource.getLeader(user_email).apply {
            if (this == null) {
                localDataSource.getTrainee(user_email).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun get(user_email: String, user_pass: String): User? {
        localDataSource.getLeader(user_email, user_pass).apply {
            if (this == null) {
                localDataSource.getTrainee(user_email, user_pass).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun insertLeader(leader: Leader): Long {
        if (get(leader.email) != null) {
            // Return duplicate when exist email on db
            return StatusCode.DUPLICATE.value
        }
        val result = remoteDataSource.insertLeader(leader)
        if (result == StatusCode.SUCCESS.value) {
            // Insert local with Room
            localDataSource.insertLeader(leader)
        }
        return result
    }


    suspend fun insertTrainee(trainee: Trainee): Long {
        if (get(trainee.email) != null) {
            // Return duplicate when exist email on db
            return StatusCode.DUPLICATE.value
        }
        val result = remoteDataSource.insertTrainee(trainee)
        if (result == StatusCode.SUCCESS.value) {
            // Insert local with Room
            localDataSource.insertTrainee(trainee)
        }
        return result
    }

    suspend fun updateTraineeName(idTrainee: Int, name: String) =
        localDataSource.updateTraineeName(idTrainee, name)

    suspend fun updateTraineeLastName(idTrainee: Int, lastName: String) =
        localDataSource.updateTraineeLastName(idTrainee, lastName)

    suspend fun deleteTrainee(trainee: Trainee) {
        localDataSource.deleteTrainee(trainee)
    }

    suspend fun setLinkedTrainee(trainee: Trainee, leader: Leader) {
        localDataSource.setLinkedTrainee(trainee.id, leader.id)
    }

    suspend fun getUnlinkedTrainees(): List<Trainee> {
        return localDataSource.getUnlinkedTrainees()
    }

    suspend fun getLinkedTrainees(leader: Leader): List<Trainee> =
        localDataSource.getLinkedTrainees(leader.id)

    suspend fun setUnlinkedTrainee(trainee: Trainee) =
        localDataSource.setUnlinkedTrainee(trainee.id)

    suspend fun updateTraineePassword(password: String, id: Int) =
        localDataSource.updateTraineePassword(password, id)

    suspend fun updateTraineePosition(trainee: Trainee, position: Position) =
        localDataSource.updateTraineePosition(trainee.id, position)

    suspend fun updateLeaderName(leaderId: Int, name: String) =
        localDataSource.updateLeaderName(leaderId, name)

    suspend fun updateLeaderLastName(leaderId: Int, lastName: String) =
        localDataSource.updateLeaderLastName(leaderId, lastName)

    suspend fun updateLeaderPass(leaderId: Int, pass: String) =
        localDataSource.updateLeaderPassword(leaderId, pass)
}
