package com.aferrari.trailead.domain.repository

import com.aferrari.trailead.app.configurer.NetworkManager
import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.RegisterState
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.common_enum.UserType
import com.aferrari.trailead.common.common_enum.toStatusCode
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getUser(userId: String): Flow<User?> = flow {
        remoteDataSource.getUserType(userId)
            .collect { userType ->
                when (userType) {
                    UserType.LEADER -> {
                        remoteDataSource.getLeader(userId)
                            ?.let { emit(it) }
                    }

                    UserType.TRAINEE -> {
                        remoteDataSource.getTrainee(userId)
                            ?.let { emit(it) }
                    }

                    else -> {
                        emit(null)
                        return@collect
                    }
                }
            }

    }

    suspend fun getUserByEmail(userEmail: String): User? {
        remoteDataSource.getLeaderByEmail(userEmail).apply {
            if (this == null) {
                remoteDataSource.getTraineeByEmail(userEmail).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun createUser(
        userType: UserType,
        name: String,
        lastName: String,
        email: String,
        pass: String
    ): Flow<RegisterState> =
        flow {
            try {
                val authResult =
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener {}.await()
                if (authResult.user != null) {
                    emit(
                        getSuccessRegisterResult(
                            authResult,
                            userType,
                            name,
                            lastName
                        )
                    )
                } else emit(RegisterState.FAILED)
            } catch (e: Exception) {
                if (e.message != null && e.message!!.contains(
                        FIREBASE_CREATE_USER_DUPLICATE_ERROR_MESSAGE
                    )
                ) emit(RegisterState.FAILED_USER_EXIST)
                else emit(RegisterState.FAILED)
            }
        }

    private suspend fun UserRepository.getSuccessRegisterResult(
        authResult: AuthResult,
        userType: UserType,
        name: String,
        lastName: String
    ): RegisterState {
        if (authResult.user != null) {
            if (authResult.additionalUserInfo?.isNewUser == false) {
                return RegisterState.FAILED_USER_EXIST
            }
            return when (userType) {
                UserType.LEADER -> {
                    createLeader(authResult.user!!, name, lastName, userType)
                }

                UserType.TRAINEE -> {
                    createTrainee(authResult.user!!, name, lastName, userType)
                }
            }
        } else {
            return RegisterState.FAILED
        }
    }

    private suspend fun createTrainee(
        it: FirebaseUser,
        name: String,
        lastName: String,
        userType: UserType
    ): RegisterState {
        val trainee = Trainee(
            it.uid,
            name,
            lastName,
            it.email ?: "",
            userType = userType
        )
        remoteDataSource.insertTrainee(trainee).apply {
            return if (this == StatusCode.SUCCESS.value) RegisterState.SUCCESS else {
                it.delete()
                RegisterState.FAILED
            }
        }
    }

    private suspend fun createLeader(
        it: FirebaseUser,
        name: String,
        lastName: String,
        userType: UserType
    ): RegisterState {
        val leader = Leader(
            it.uid,
            name,
            lastName,
            it.email ?: "",
            userType
        )
        remoteDataSource.insertLeader(leader).apply {
            return if (this == StatusCode.SUCCESS.value) RegisterState.SUCCESS else {
                it.delete()
                RegisterState.FAILED
            }
        }
    }

    suspend fun updateTraineeName(idTrainee: String, name: String) {
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION
        } else {
            remoteDataSource.updateTraineeName(idTrainee, name)
        }
    }

    suspend fun updateTraineeLastName(idTrainee: String, lastName: String) {
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION
        } else {
            remoteDataSource.updateTraineeLastName(idTrainee, lastName)
        }
    }

    suspend fun deleteTrainee(trainee: Trainee) {
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION
        } else {
            remoteDataSource.deleteTrainee(trainee)
        }
    }

    suspend fun setLinkedTrainee(trainee: Trainee, leader: Leader) {
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION
        } else {
            remoteDataSource.setLinkedTrainee(trainee.userId, leader.userId)
        }
    }

    suspend fun getUnlinkedTrainees(): List<Trainee> =
        if (!NetworkManager.isOnline()) {
            emptyList()
        } else {
            remoteDataSource.getUnlinkedTrainees()
        }

    suspend fun getLinkedTrainees(leader: Leader): List<Trainee> =
        if (!NetworkManager.isOnline()) {
            emptyList()
        } else {
            remoteDataSource.getLinkedTrainees(leader.userId)
        }

    suspend fun setUnlinkedTrainee(trainee: Trainee) {
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION
        } else {
            remoteDataSource.setUnlinkedTrainee(trainee.userId)
        }
    }

    suspend fun updateTraineePassword(traineeId: String, password: String) {
        remoteDataSource.updateTraineePassword(traineeId, password)
    }

    suspend fun updateTraineePosition(trainee: Trainee, position: Position) {
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION
        } else {
            remoteDataSource.updateTraineePosition(trainee.userId, position)
        }
    }

    suspend fun updateLeaderName(leaderId: String, name: String) {
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION
        } else {
            remoteDataSource.updateLeaderName(leaderId, name)
        }
    }

    suspend fun updateLeaderLastName(leaderId: String, lastName: String) =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION
        } else {
            remoteDataSource.updateLeaderLastName(leaderId, lastName)
        }

    suspend fun updateUserPass(pass: String): StatusCode =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION
        } else {
            remoteDataSource.updateUserPassword(pass).toStatusCode()
        }


    suspend fun getTrainee(traineeId: String): Trainee? = remoteDataSource.getTrainee(traineeId)
    suspend fun getLeader(leaderId: String): Leader? = remoteDataSource.getLeader(leaderId)
    suspend fun validateAccessKey(user: User, accessKey: Int): Long = when (user.userType) {
        UserType.LEADER -> remoteDataSource.validateLeaderAccessKey(user.userId, accessKey)
        UserType.TRAINEE -> remoteDataSource.validateTraineeAccessKey(user.userId, accessKey)
    }

    suspend fun updateAccessKey(user: User, accessKey: Int) = when (user.userType) {
        UserType.LEADER -> remoteDataSource.updateLeaderAccessKey(user.userId, accessKey)
        UserType.TRAINEE -> remoteDataSource.updateTraineeAccessKey(user.userId, accessKey)
    }

    suspend fun signInWithEmail(email: String, pass: String): Flow<StatusCode> =
        if (!NetworkManager.isOnline()) {
            MutableStateFlow(StatusCode.INTERNET_CONECTION)
        } else {
            remoteDataSource.signInWithEmailAndPassword(email, pass)
        }

    suspend fun signInWithToken(userId: String, token: String): Flow<StatusCode> =
        if (!NetworkManager.isOnline()) {
            flowOf(StatusCode.INTERNET_CONECTION)
        } else {
            remoteDataSource.signInWithToken(userId, token)
        }

    suspend fun getUserToken(): Flow<String> =
        if (!NetworkManager.isOnline()) {
            flowOf("")
        } else {
            remoteDataSource.getUserToken()
        }

    suspend fun getTokenByUser(userId: String): Flow<String> =
        if (!NetworkManager.isOnline()) {
            flowOf("")
        } else {
            remoteDataSource.getTokenByUser(userId)
        }

    suspend fun updateUserToken(): Flow<StatusCode> =
        if (!NetworkManager.isOnline()) {
            flowOf(StatusCode.INTERNET_CONECTION)
        } else {
            remoteDataSource.updateUserToken()
        }

    suspend fun sendNotify(fromToken: String, toToken: String, title: String, message: String) {
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION
        } else {
            remoteDataSource.notify(fromToken, toToken, title, message)
        }
    }


    private companion object {
        private const val FIREBASE_CREATE_USER_DUPLICATE_ERROR_MESSAGE =
            "The email address is already in use by another account"
    }

}
