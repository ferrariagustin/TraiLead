package com.aferrari.login.db

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

    @Query("UPDATE trainee_data_table SET position = :trainee_position WHERE trainee_id =:trainee_id")
    suspend fun updateTraineePosition(trainee_id: Int, trainee_position: Position)

    @Query("UPDATE leader_data_table SET leader_name = :name WHERE leader_id =:leaderId")
    suspend fun updateLeaderName(leaderId: Int, name: String)

    @Query("UPDATE leader_data_table SET leader_last_name = :lastName WHERE leader_id =:leaderId")
    suspend fun updateLeaderLastName(leaderId: Int, lastName: String)

    @Query("UPDATE leader_data_table SET leader_pass = :pass WHERE leader_id =:leaderId")
    suspend fun updateLeaderPassword(leaderId: Int, pass: String)

    //    Category

    @Insert
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM category_data_table WHERE leader_category_id = :leaderId ORDER BY category_name")
    suspend fun getAllCategory(leaderId: Int): List<Category>

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("UPDATE category_data_table SET category_name = :categoryName WHERE category_id = :categoryId")
    suspend fun updateCategory(categoryId: Int, categoryName: String)

    @Insert
    suspend fun insertMaterial(newMaterial: Material)

    @Query("SELECT * FROM material_data_table WHERE leader_material_id = :leaderId ORDER BY material_title")
    suspend fun getAllMaterial(leaderId: Int): List<Material>

    @Delete
    suspend fun deleteMaterial(material: Material)

    @Query("UPDATE material_data_table SET material_url = :youtubeId WHERE material_id = :materialId")
    suspend fun updateUrlMaterial(materialId: Int, youtubeId: String)

    @Query("UPDATE material_data_table SET material_title = :newTitle WHERE material_id = :materialId")
    suspend fun updateTitleMaterial(materialId: Int, newTitle: String)
}