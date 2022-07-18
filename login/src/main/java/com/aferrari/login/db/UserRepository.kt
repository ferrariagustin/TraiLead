package com.aferrari.login.db

class UserRepository(private val dao: UserDao) {

    val users = dao.getAllUser()

    suspend fun insert(user: User) {
        dao.insertUser(user)
    }

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
