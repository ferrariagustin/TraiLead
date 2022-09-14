package com.aferrari.login.db

class UserRepository(private val dao: UserDao) {

    private val trainees = dao.getAllTrainee().value
    private val leaders = dao.getAllLeader().value
    val users = getAllUsers()

    private fun getAllUsers(): List<User> = arrayListOf<User>().apply {
        trainees?.let { this.addAll(it as List<User>) }
        leaders?.let { this.addAll(it as List<User>) }
    }

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

    suspend fun insertLeader(leader: Leader): Long = dao.insertLeader(leader)

    suspend fun insertTrainee(trainee: Trainee): Long = dao.insertTrainee(trainee)

    suspend fun updateLeader(leader: Leader) = dao.updateLeader(leader)

    suspend fun updateTrainee(trainee: Trainee) = dao.updateTrainee(trainee)

    suspend fun deleteLeader(leader: Leader) {
        dao.deleteLeader(leader)
    }

    suspend fun deleteTrainee(trainee: Trainee) {
        dao.deleteTrainee(trainee)
    }

    suspend fun deleteAllLeader() {
        dao.deleteAllLeader()
    }

    suspend fun deleteAllTrainee() {
        dao.deleteAllTrainee()
    }

    suspend fun getAllTrainee() = dao.getAllTrainee()

    suspend fun getAllLeader() = dao.getAllLeader()

}
