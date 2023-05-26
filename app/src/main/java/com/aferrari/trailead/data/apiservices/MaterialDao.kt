package com.aferrari.trailead.data.apiservices

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo

@Dao
interface MaterialDao {
    @Insert
    suspend fun insertYouTubeVideo(newYouTubeVideo: YouTubeVideo)

    @Query("SELECT * FROM youtube_video_data_table WHERE leaderMaterialId = :leaderId ORDER BY title")
    suspend fun getAllYoutubeVideo(leaderId: Int): List<YouTubeVideo>

    @Delete
    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo)

    @Query("UPDATE youtube_video_data_table SET url = :youtubeId WHERE id = :materialId")
    suspend fun updateUrlYoutubeVideo(materialId: Int, youtubeId: String)

    @Query("UPDATE youtube_video_data_table SET title = :newTitle WHERE id = :youtubeVideoId")
    suspend fun updateTitleYoutubeVideo(youtubeVideoId: Int, newTitle: String)

    @Query("SELECT * FROM youtube_video_data_table WHERE leaderMaterialId=:leaderId AND categoryId=:categoryId")
    suspend fun getYoutubeVideoByCategory(leaderId: Int, categoryId: Int): List<YouTubeVideo>

    //    Category

    @Insert
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
    suspend fun removeAllCategoryFromTrainee(traineeId: Int)

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
}