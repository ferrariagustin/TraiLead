package com.aferrari.trailead.domain.repository

import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.common_enum.UserType
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    fun getUser(user_id: Int): Flow<User?> = flow {
        remoteDataSource.getUserType(user_id)
            .collect { userType ->
                when (userType) {
                    UserType.LEADER -> {
                        remoteDataSource.getLeader(user_id)
                            ?.let { emit(it) }
                    }

                    UserType.TRAINEE -> {
                        remoteDataSource.getTrainee(user_id)
                            ?.let { emit(it) }
                    }

                    else -> {
                        emit(null)
                        return@collect
                    }
                }
            }
//        if (localDataSource.isEmpty()) {
//        }
//        when (localDataSource.getUserType(user_id)) {
//            UserType.LEADER -> {
//                emit(localDataSource.getLeader(user_id))
//            }
//
//            UserType.TRAINEE -> {
//                emit(localDataSource.getTrainee(user_id))
//            }
//
//            else -> {
//                emit(null)
//            }
//        }

    }

    suspend fun getUser(user_email: String): User? {
        remoteDataSource.getLeader(user_email).apply {
            if (this == null) {
                remoteDataSource.getTrainee(user_email).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun getUser(user_email: String, user_pass: String): User? {
        remoteDataSource.getLeader(user_email, user_pass).apply {
            if (this == null) {
                remoteDataSource.getTrainee(user_email, user_pass).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun insertLeader(leader: Leader): Long {
        if (getUser(leader.email) != null) {
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
        if (getUser(trainee.email) != null) {
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

    suspend fun updateTraineeName(idTrainee: Int, name: String) {
        val result = remoteDataSource.updateTraineeName(idTrainee, name)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateTraineeName(idTrainee, name)
        }
    }

    suspend fun updateTraineeLastName(idTrainee: Int, lastName: String) {
        val result = remoteDataSource.updateTraineeLastName(idTrainee, lastName)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateTraineeLastName(idTrainee, lastName)
        }
    }

    suspend fun deleteTrainee(trainee: Trainee) {
        val result = remoteDataSource.deleteTrainee(trainee)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.deleteTrainee(trainee)
        }
    }

    suspend fun setLinkedTrainee(trainee: Trainee, leader: Leader) {
        val result = remoteDataSource.setLinkedTrainee(trainee.id, leader.id)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.setLinkedTrainee(trainee.id, leader.id)
        }
    }

    suspend fun getUnlinkedTrainees(): List<Trainee> = remoteDataSource.getUnlinkedTrainees()

    suspend fun getLinkedTrainees(leader: Leader): List<Trainee> =
        remoteDataSource.getLinkedTrainees(leader.id)


    suspend fun setUnlinkedTrainee(trainee: Trainee) {
        val result = remoteDataSource.setUnlinkedTrainee(trainee.id)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.setUnlinkedTrainee(trainee.id)
        }
    }

    suspend fun updateTraineePassword(password: String, traineeId: Int) {
        val result = remoteDataSource.updateTraineePassword(traineeId, password)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateTraineePassword(traineeId, password)
        }
    }

    suspend fun updateTraineePosition(trainee: Trainee, position: Position) {
        val result = remoteDataSource.updateTraineePosition(trainee.id, position)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateTraineePosition(trainee.id, position)
        }
    }

    suspend fun updateLeaderName(leaderId: Int, name: String) {
        val result = remoteDataSource.updateLeaderName(leaderId, name)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateLeaderName(leaderId, name)
        }
    }

    suspend fun updateLeaderLastName(leaderId: Int, lastName: String) {
        val result = remoteDataSource.updateLeaderLastName(leaderId, lastName)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateLeaderLastName(leaderId, lastName)
        }
    }

    suspend fun updateLeaderPass(leaderId: Int, pass: String) {
        val result = remoteDataSource.updateLeaderPassword(leaderId, pass)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateLeaderPassword(leaderId, pass)
        }
    }

    suspend fun getTrainee(traineeId: Int): Trainee? = remoteDataSource.getTrainee(traineeId)
    suspend fun getLeader(leaderId: Int): Leader? = remoteDataSource.getLeader(leaderId)
}
