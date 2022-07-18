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

    @Query("SELECT * FROM user_data_table")
    fun getAllUser(): LiveData<List<User>>
}