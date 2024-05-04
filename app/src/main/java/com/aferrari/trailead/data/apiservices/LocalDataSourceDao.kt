package com.aferrari.trailead.data.apiservices

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo

@Dao
interface LocalDataSourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYouTubeVideo(newYouTubeVideo: YouTubeVideo)

    @Query("SELECT * FROM youtube_video_data_table WHERE leaderMaterialId = :leaderId ORDER BY title")
    suspend fun getAllYoutubeVideo(leaderId: Int): List<YouTubeVideo>

    @Delete
    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo)

    @Query("UPDATE youtube_video_data_table SET url = :youtubeUrl WHERE id = :materialId")
    suspend fun updateUrlYoutubeVideo(materialId: Int, youtubeUrl: String)

    @Query("UPDATE youtube_video_data_table SET title = :newTitle WHERE id = :youtubeVideoId")
    suspend fun updateTitleYoutubeVideo(youtubeVideoId: Int, newTitle: String)

    @Query("SELECT * FROM youtube_video_data_table WHERE leaderMaterialId=:leaderId AND categoryId=:categoryId")
    suspend fun getYoutubeVideoByCategory(
        leaderId: Int,
        categoryId: Int
    ): List<YouTubeVideo>

    //    Category

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM category_data_table WHERE leader_category_id = :leaderId ORDER BY category_name")
    suspend fun getAllCategory(leaderId: Int): List<Category>

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("UPDATE category_data_table SET category_name = :categoryName WHERE category_id = :categoryId")
    suspend fun updateCategory(categoryId: Int, categoryName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategoryFromTrainee(traineeCategoryJoin: List<TraineeCategoryJoin>)

    @Query("SELECT * FROM category_data_table INNER JOIN trainee_category_join ON category_data_table.category_id = trainee_category_join.category_id WHERE trainee_category_join.trainee_id = :traineeId")
    suspend fun getCategoriesFromTrainee(traineeId: Int): List<Category>


    @Query("DELETE FROM trainee_category_join WHERE trainee_category_join.trainee_id = :traineeId")
    suspend fun deleteAllTraineeCategoryJoinForTrainee(traineeId: Int)

    @Query("DELETE FROM trainee_category_join WHERE trainee_category_join.category_id = :categoryId")
    suspend fun deleteAllTraineeCategoryJoinForCategory(categoryId: Int)


    //  Link
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(link: Link)

    @Delete
    suspend fun deleteLink(link: Link)

    @Query("SELECT * FROM link_data_table WHERE leaderMaterialId = :leaderId ORDER BY title")
    suspend fun getAllLink(leaderId: Int): List<Link>

    @Query("UPDATE link_data_table SET url = :link WHERE id = :linkId")
    suspend fun updateUrlLink(linkId: Int, link: String)

    @Query("UPDATE link_data_table SET title = :newTitle WHERE id = :linkId")
    suspend fun updateTitleLink(linkId: Int, newTitle: String)

    @Query("SELECT * FROM link_data_table WHERE leaderMaterialId=:leaderId AND categoryId=:categoryId")
    suspend fun getLinkByCategory(leaderId: Int, categoryId: Int): List<Link>

    @Insert
    suspend fun insertLeader(leader: Leader): Long

    @Insert
    suspend fun insertTrainee(trainee: Trainee): Long

    @Query("UPDATE trainee_data_table SET trainee_name=:name WHERE trainee_id = :idTrainee")
    suspend fun updateTraineeName(idTrainee: Int, name: String)

    @Query("UPDATE trainee_data_table SET trainee_last_name=:lastName WHERE trainee_id = :idTrainee")
    suspend fun updateTraineeLastName(idTrainee: Int, lastName: String)


    @Delete
    suspend fun deleteLeader(leader: Leader)

    @Delete
    suspend fun deleteTrainee(trainee: Trainee)

    @Query("DELETE FROM leader_data_table")
    suspend fun deleteAllLeader()

    @Query("DELETE FROM trainee_data_table")
    suspend fun deleteAllTrainee()

//    @Query("SELECT * FROM leader_data_table WHERE leader_id = :leader_id")
//    suspend fun getLeader(leader_id: Int): Leader?

    @Query("SELECT * FROM trainee_data_table WHERE trainee_id = :trainee_id")
    suspend fun getTrainee(trainee_id: Int): Trainee?

//    @Query("SELECT * FROM leader_data_table WHERE leader_email = :user_email and leader_pass = :user_pass")
//    suspend fun getLeader(user_email: String, user_pass: String): Leader?

//    @Query("SELECT * FROM trainee_data_table WHERE trainee_email = :user_email and trainee_pass = :user_pass")
//    suspend fun getTrainee(user_email: String, user_pass: String): Trainee?

    @Query("SELECT * FROM leader_data_table WHERE leader_email = :leader_email")
    suspend fun getLeader(leader_email: String): Leader?

    @Query("SELECT * FROM trainee_data_table WHERE trainee_email = :trainee_email")
    suspend fun getTrainee(trainee_email: String): Trainee?

    @Query("SELECT * FROM trainee_data_table")
    suspend fun getAllTrainee(): List<Trainee>

    @Query("SELECT COUNT(leader_id) FROM leader_data_table")
    suspend fun leaderCount(): Int

    @Query("SELECT COUNT(trainee_id) FROM trainee_data_table")
    suspend fun traineeCount(): Int

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

//    @Query("UPDATE leader_data_table SET leader_pass = :pass WHERE leader_id =:leaderId")
//    suspend fun updateLeaderPassword(leaderId: Int, pass: String)
}
