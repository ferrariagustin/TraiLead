package com.aferrari.login.database

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    suspend fun insertLeader(leader: Leader): Long

    @Insert
    suspend fun insertTrainee(trainee: Trainee): Long

    @Update
    suspend fun updateLeader(leader: Leader)

    @Update
    suspend fun updateTrainee(trainee: Trainee)

    @Query("UPDATE trainee_data_table SET trainee_name=:name WHERE trainee_id = :idTrainee")
    suspend fun updateTraineeName(idTrainee: Int, name: String)

    @Query("UPDATE trainee_data_table SET trainee_last_name=:lastName WHERE trainee_id = :idTrainee")
    suspend fun updateTraineeLastName(idTrainee: Int, lastName: String)

    @Query("UPDATE trainee_data_table SET trainee_pass=:password WHERE trainee_id = :idTrainee")
    suspend fun updateTraineePassword(password: String, idTrainee: Int)

    @Delete
    suspend fun deleteLeader(leader: Leader)

    @Delete
    suspend fun deleteTrainee(trainee: Trainee)

    @Query("DELETE FROM leader_data_table")
    suspend fun deleteAllLeader()

    @Query("DELETE FROM trainee_data_table")
    suspend fun deleteAllTrainee()

    @Query("SELECT * FROM leader_data_table")
    fun getLeadersWithTrainee(): List<LeaderWithTrainee>

    @Query("SELECT * FROM leader_data_table WHERE leader_id = :leader_id")
    suspend fun getLeader(leader_id: Int): Leader?

    @Query("SELECT * FROM trainee_data_table WHERE trainee_id = :trainee_id")
    suspend fun getTrainee(trainee_id: Int): Trainee?

    @Query("SELECT * FROM leader_data_table WHERE leader_email = :user_email and leader_pass = :user_pass")
    suspend fun getLeader(user_email: String, user_pass: String): Leader?

    @Query("SELECT * FROM trainee_data_table WHERE trainee_email = :user_email and trainee_pass = :user_pass")
    suspend fun getTrainee(user_email: String, user_pass: String): Trainee?

    @Query("SELECT * FROM leader_data_table WHERE leader_email = :leader_email")
    suspend fun getLeader(leader_email: String): Leader?

    @Query("SELECT * FROM trainee_data_table WHERE trainee_email = :trainee_email")
    suspend fun getTrainee(trainee_email: String): Trainee?

    @Query("SELECT * FROM trainee_data_table")
    suspend fun getAllTrainee(): List<Trainee>

    @Query("SELECT * FROM leader_data_table")
    suspend fun getAllLeader(): List<Leader>

    @Query("UPDATE trainee_data_table SET trainee_leader_id=:leader_id WHERE trainee_id = :trainee_id")
    suspend fun setLinkedTrainee(trainee_id: Int, leader_id: Int)

    @Query("SELECT * FROM trainee_data_table WHERE trainee_leader_id IS NULL")
    suspend fun getUnlinkedTrainees(): List<Trainee>

    @Query("SELECT * FROM trainee_data_table WHERE trainee_leader_id=:leader_id")
    suspend fun getLinkedTrainees(leader_id: Int): List<Trainee>

    @Query("UPDATE trainee_data_table SET trainee_leader_id = NULL WHERE trainee_id =:trainee_id")
    suspend fun setUnlinkedTrainee(trainee_id: Int)
}