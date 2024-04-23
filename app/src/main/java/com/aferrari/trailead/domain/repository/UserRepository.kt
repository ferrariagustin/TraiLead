package com.aferrari.trailead.domain.repository

import com.aferrari.trailead.common.PasswordUtil
import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.RegisterState
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.common_enum.UserType
import com.aferrari.trailead.common.common_enum.toStatusCode
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepository(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun initLocalDataSource() {
//        val leaderList = remoteDataSource.getAllLeader()
//        leaderList.forEach {
//            if (localDataSource.getLeader(it.email) == null) {
//                localDataSource.insertLeader(it)
//            }
//        }
//        val traineeList = remoteDataSource.getAllTrainee()
//        traineeList.forEach {
//            if (localDataSource.getTrainee(it.email) == null) {
//                localDataSource.insertTrainee(it)
//            }
//        }
    }

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

    suspend fun signIn(email: String, pass: String): Task<AuthResult> =
        withContext(Dispatchers.IO) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
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

    // TODO: Remove this method
//    suspend fun getUser(user_email: String, user_pass: String): User? {
//        remoteDataSource.getLeader(user_email, user_pass).apply {
//            if (this == null) {
//                remoteDataSource.getTrainee(user_email, user_pass).let {
//                    return it
//                }
//            } else {
//                return this
//            }
//        }
//    }

    suspend fun createUser2(
        userType: UserType,
        name: String,
        lastName: String,
        email: String,
        pass: String
    ): Flow<RegisterState> =
        withContext(Dispatchers.IO) {
            var registerResult: Flow<RegisterState> = MutableStateFlow(RegisterState.STARTED)
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    launch {
                        registerResult = if (it.isSuccessful) {
                            MutableStateFlow(
                                getSuccessRegisterResult(
                                    it.result,
                                    userType,
                                    name,
                                    lastName
                                )
                            )
                        } else {
                            MutableStateFlow(RegisterState.FAILED)
                        }
                        registerResult.collect()
                    }

                }

            return@withContext registerResult
        }

    suspend fun createUser(
        userType: UserType,
        name: String,
        lastName: String,
        email: String,
        pass: String
    ): Flow<RegisterState> =
        withContext(Dispatchers.IO) {
            val registerResult = MutableStateFlow(RegisterState.STARTED)
            try {
                var wasSuccessCreatedUser = false
                val authResult =
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                wasSuccessCreatedUser = true
                            }
                        }.await()
                if (wasSuccessCreatedUser) {
                    registerResult.emit(
                        getSuccessRegisterResult(
                            authResult,
                            userType,
                            name,
                            lastName
                        )
                    )
                } else {
                    registerResult.emit(RegisterState.FAILED)
                }
            } catch (e: Exception) {
                if (e.message != null && e.message!!.contains(
                        FIREBASE_CREATE_USER_DUPLICATE_ERROR_MESSAGE
                    )
                ) {
                    registerResult.emit(RegisterState.FAILED_USER_EXIST)
                } else {
                    registerResult.emit(RegisterState.FAILED)
                }
            }
            return@withContext registerResult
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
            when (userType) {
                UserType.LEADER -> {
                    return createLeader(authResult.user!!, name, lastName, userType)
                }

                UserType.TRAINEE -> {
                    return createTrainee(authResult.user!!, name, lastName, userType)
                }
            }
        } else {
            return RegisterState.FAILED
        }
    }
//        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).await().apply {
//            user?.let {
//                additionalUserInfo?.isNewUser?.let { isNewUser ->
//                    if (isNewUser.not()) {
//                        return RegisterState.FAILED_USER_EXIST
//                    }
//                }
//                when (userType) {
//                    UserType.LEADER -> {
//                        return createLeader(it, name, lastName, userType)
//                    }
//
//                    UserType.TRAINEE -> {
//                        return createTrainee(it, name, lastName, userType)
//                    }
//                }
//            } ?: run {
//                return RegisterState.FAILED
//            }
//            return RegisterState.FAILED
//        }

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
        val result = remoteDataSource.updateTraineeName(idTrainee, name)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateTraineeName(idTrainee, name)
        }
    }

    suspend fun updateTraineeLastName(idTrainee: String, lastName: String) {
        val result = remoteDataSource.updateTraineeLastName(idTrainee, lastName)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateTraineeLastName(idTrainee, lastName)
        }
    }

    suspend fun deleteTrainee(trainee: Trainee) {
        val result = remoteDataSource.deleteTrainee(trainee)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.deleteTrainee(trainee)
        }
    }

    suspend fun setLinkedTrainee(trainee: Trainee, leader: Leader) {
        val result = remoteDataSource.setLinkedTrainee(trainee.userId, leader.userId)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.setLinkedTrainee(trainee.userId, leader.userId)
        }
    }

    suspend fun getUnlinkedTrainees(): List<Trainee> = remoteDataSource.getUnlinkedTrainees()

    suspend fun getLinkedTrainees(leader: Leader): List<Trainee> =
        remoteDataSource.getLinkedTrainees(leader.userId)


    suspend fun setUnlinkedTrainee(trainee: Trainee) {
        val result = remoteDataSource.setUnlinkedTrainee(trainee.userId)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.setUnlinkedTrainee(trainee.userId)
        }
    }

    suspend fun updateTraineePassword(traineeId: String, password: String) {
        val passwordHash = PasswordUtil.hashPassword(password)
        val result = remoteDataSource.updateTraineePassword(traineeId, passwordHash)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateTraineePassword(traineeId, passwordHash)
        }
    }

    suspend fun updateTraineePosition(trainee: Trainee, position: Position) {
        val result = remoteDataSource.updateTraineePosition(trainee.userId, position)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateTraineePosition(trainee.userId, position)
        }
    }

    suspend fun updateLeaderName(leaderId: String, name: String) {
        val result = remoteDataSource.updateLeaderName(leaderId, name)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateLeaderName(leaderId, name)
        }
    }

    suspend fun updateLeaderLastName(leaderId: String, lastName: String) {
        val result = remoteDataSource.updateLeaderLastName(leaderId, lastName)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateLeaderLastName(leaderId, lastName)
        }
    }

    suspend fun updateLeaderPass(leaderId: String, pass: String) {
//        val hashPassword = PasswordUtil.hashPassword(pass)
//        val result = remoteDataSource.updateLeaderPassword(leaderId, hashPassword)
//        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateLeaderPassword(leaderId, hashPassword)
//        }
    }

    suspend fun updateUserPass(pass: String): StatusCode =
        remoteDataSource.updateUserPassword(pass).toStatusCode()


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

    private companion object {
        private const val FIREBASE_CREATE_USER_DUPLICATE_ERROR_MESSAGE =
            "The email address is already in use by another account"
    }

}
