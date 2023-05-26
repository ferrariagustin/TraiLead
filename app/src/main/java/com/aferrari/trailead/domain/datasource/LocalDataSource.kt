package com.aferrari.trailead.domain.datasource

import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.LeaderWithTrainee
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo

interface LocalDataSource {

    suspend fun insertYouTubeVideo(newYouTubeVideo: YouTubeVideo)

    suspend fun getAllYoutubeVideo(leaderId: Int): List<YouTubeVideo>

    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo)

    suspend fun updateUrlYoutubeVideo(materialId: Int, youtubeId: String)

    suspend fun updateTitleYoutubeVideo(youtubeVideoId: Int, newTitle: String)

    suspend fun getYoutubeVideoByCategory(leaderId: Int, categoryId: Int): List<YouTubeVideo>

    //    Category

    suspend fun insertCategory(category: Category)

    suspend fun getAllCategory(leaderId: Int): List<Category>

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(categoryId: Int, categoryName: String)

    suspend fun insertAllCategoryFromTrainee(traineeCategoryJoin: List<TraineeCategoryJoin>)

    suspend fun getCategoriesFromTrainee(traineeId: Int): List<Category>

    suspend fun removeAllCategoryFromTrainee(traineeId: Int)

    //  Link
    suspend fun insertLink(link: Link)

    suspend fun deleteLink(link: Link)

    suspend fun getAllLink(leaderId: Int): List<Link>

    suspend fun updateUrlLink(linkId: Int, link: String)

    suspend fun updateTitleLink(linkId: Int, newTitle: String)

    suspend fun getLinkByCategory(leaderId: Int, categoryId: Int): List<Link>

    suspend fun insertLeader(leader: Leader): Long

    suspend fun insertTrainee(trainee: Trainee): Long

    suspend fun updateLeader(leader: Leader)

    suspend fun updateTrainee(trainee: Trainee)

    suspend fun updateTraineeName(idTrainee: Int, name: String)

    suspend fun updateTraineeLastName(idTrainee: Int, lastName: String)

    suspend fun updateTraineePassword(password: String, idTrainee: Int)

    suspend fun deleteLeader(leader: Leader)

    suspend fun deleteTrainee(trainee: Trainee)

    suspend fun deleteAllLeader()

    suspend fun deleteAllTrainee()

    suspend fun getLeadersWithTrainee(): List<LeaderWithTrainee>

    suspend fun getLeader(leader_id: Int): Leader?

    suspend fun getTrainee(trainee_id: Int): Trainee?

    suspend fun getLeader(user_email: String, user_pass: String): Leader?

    suspend fun getTrainee(user_email: String, user_pass: String): Trainee?

    suspend fun getLeader(leader_email: String): Leader?

    suspend fun getTrainee(trainee_email: String): Trainee?

    suspend fun getAllTrainee(): List<Trainee>

    suspend fun getAllLeader(): List<Leader>

    suspend fun setLinkedTrainee(trainee_id: Int, leader_id: Int)

    suspend fun getUnlinkedTrainees(): List<Trainee>

    suspend fun getLinkedTrainees(leader_id: Int): List<Trainee>

    suspend fun setUnlinkedTrainee(trainee_id: Int)

    suspend fun updateTraineePosition(trainee_id: Int, trainee_position: Position)

    suspend fun updateLeaderName(leaderId: Int, name: String)

    suspend fun updateLeaderLastName(leaderId: Int, lastName: String)

    suspend fun updateLeaderPassword(leaderId: Int, pass: String)
}