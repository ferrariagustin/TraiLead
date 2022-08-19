package com.aferrari.login.db

class UserRepository(private val dao: UserDao) {

    val users = dao.getAllUser()

    suspend fun get(user_id: Int): User? = dao.getUser(user_id)

    suspend fun get(user_email: String, user_pass: String) = dao.getUser(user_email, user_pass)

    suspend fun insert(user: User): Long = dao.insertUser(user)

    suspend fun update(user: User) {
        dao.updateUser(user)
    }

    suspend fun delete(user: User) {
        dao.deleteUser(user)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
