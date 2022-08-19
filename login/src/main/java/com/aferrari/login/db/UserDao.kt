package com.aferrari.login.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user_data_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM user_data_table WHERE user_id = :user_id")
    suspend fun getUser(user_id: Int): User?

    @Query("SELECT * FROM user_data_table WHERE user_email = :user_email and user_pass = :user_pass")
    suspend fun getUser(user_email: String, user_pass: String): User?

    @Query("SELECT * FROM user_data_table")
    fun getAllUser(): LiveData<List<User>>
}