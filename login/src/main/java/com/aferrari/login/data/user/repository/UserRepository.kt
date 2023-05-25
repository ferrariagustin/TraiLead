package com.aferrari.login.data.user.repository

import com.aferrari.login.data.FirebaseDataBase
import com.aferrari.login.data.StatusCode
import com.aferrari.login.data.user.Leader
import com.aferrari.login.data.user.Position
import com.aferrari.login.data.user.Trainee
import com.aferrari.login.data.user.dao.User
import com.aferrari.login.data.user.dao.UserDao
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO: change name LocalDataSource
// class UserRepository(private val dao: UserDao, remote: RemoteDataSource) {
class UserRepository(private val dao: UserDao) {

    suspend fun get(user_id: Int): User? {
        dao.getLeader(user_id).apply {
            if (this == null) {
                dao.getTrainee(user_id).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun get(user_email: String): User? {
        dao.getLeader(user_email).apply {
            if (this == null) {
                dao.getTrainee(user_email).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun get(user_email: String, user_pass: String): User? {
        dao.getLeader(user_email, user_pass).apply {
            if (this == null) {
                dao.getTrainee(user_email, user_pass).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun insertLeader(leader: Leader): Long {

//        val result = remote.insert(leader)
//        if (result == SUCCESS) {
//            localDS.insert()
//        } else {
//            return error
//        }

        val result = suspendCoroutine { continuation ->
            FirebaseDataBase().insertLeader(leader)?.addOnCompleteListener { task ->
                val resultCode = if (task.isSuccessful) {
                    StatusCode.SUCCESS.value
                } else {
                    StatusCode.ERROR.value
                }
                continuation.resume(resultCode)
            }
        }
        if (result == StatusCode.SUCCESS.value) {
            // Insert local with Room
            dao.insertLeader(leader)
        }
        return result
    }


    suspend fun insertTrainee(trainee: Trainee): Long = dao.insertTrainee(trainee)

    suspend fun updateTraineeName(idTrainee: Int, name: String) =
        dao.updateTraineeName(idTrainee, name)

    suspend fun updateTraineeLastName(idTrainee: Int, lastName: String) =
        dao.updateTraineeLastName(idTrainee, lastName)

    suspend fun deleteTrainee(trainee: Trainee) {
        dao.deleteTrainee(trainee)
    }

    suspend fun setLinkedTrainee(trainee: Trainee, leader: Leader) {
        dao.setLinkedTrainee(trainee.id, leader.id)
    }

    suspend fun getUnlinkedTrainees(): List<Trainee> {
        return dao.getUnlinkedTrainees()
    }

    suspend fun getLinkedTrainees(leader: Leader): List<Trainee> = dao.getLinkedTrainees(leader.id)

    suspend fun setUnlinkedTrainee(trainee: Trainee) = dao.setUnlinkedTrainee(trainee.id)

    suspend fun updateTraineePassword(password: String, id: Int) =
        dao.updateTraineePassword(password, id)

    suspend fun updateTraineePosition(trainee: Trainee, position: Position) =
        dao.updateTraineePosition(trainee.id, position)

    suspend fun updateLeaderName(leaderId: Int, name: String) = dao.updateLeaderName(leaderId, name)

    suspend fun updateLeaderLastName(leaderId: Int, lastName: String) =
        dao.updateLeaderLastName(leaderId, lastName)

    suspend fun updateLeaderPass(leaderId: Int, pass: String) =
        dao.updateLeaderPassword(leaderId, pass)
}
