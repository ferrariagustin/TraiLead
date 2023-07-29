package com.aferrari.trailead.domain.datasource

import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.UserType
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo
import kotlinx.coroutines.flow.Flow


interface RemoteDataSource {
    suspend fun insertYouTubeVideo(newYouTubeVideo: YouTubeVideo): Long

    suspend fun getAllYoutubeVideo(leaderId: Int): List<YouTubeVideo>

    suspend fun getAllYoutubeVideo(): List<YouTubeVideo>

    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo): Long

    suspend fun updateUrlYoutubeVideo(youtubeId: Int, youtubeUrl: String): Long

    suspend fun updateTitleYoutubeVideo(youtubeId: Int, newTitle: String): Long

    suspend fun getYoutubeVideoByCategory(leaderId: Int, categoryId: Int): List<YouTubeVideo>

    //    Category

    suspend fun insertCategory(category: Category): Long

    suspend fun getAllCategory(leaderId: Int): List<Category>

    suspend fun getAllCategory(): List<Category>

    suspend fun deleteCategory(category: Category): Long

    suspend fun updateCategory(categoryId: Int, categoryName: String): Long

    suspend fun insertAllCategoryFromTrainee(traineeCategoryJoinList: List<TraineeCategoryJoin>): Long

    suspend fun insertCategoryFromTrainee(traineeCategoryJoin: TraineeCategoryJoin): Long

    suspend fun getCategoriesFromTrainee(traineeId: Int): List<Category>

    suspend fun deleteAllTraineeCategoryJoin(traineeId: Int): Long

    suspend fun getAllTraineeCategoryJoin(): List<TraineeCategoryJoin>

    suspend fun deleteTraineeCategoryJoin(traineeCategoryJoinId: Int): Long

    //  Link
    suspend fun insertLink(link: Link): Long

    suspend fun deleteLink(link: Link): Long

    suspend fun getAllLink(leaderId: Int): List<Link>

    suspend fun getAllLink(): List<Link>

    suspend fun updateUrlLink(linkId: Int, link: String): Long

    suspend fun updateTitleLink(linkId: Int, newTitle: String): Long

    suspend fun getLinkByCategory(leaderId: Int, categoryId: Int): List<Link>

    suspend fun insertLeader(leader: Leader): Long

    suspend fun insertTrainee(trainee: Trainee): Long

    suspend fun updateTraineeName(idTrainee: Int, name: String): Long

    suspend fun updateTraineeLastName(idTrainee: Int, lastName: String): Long

    suspend fun updateTraineePassword(idTrainee: Int, pass: String): Long

    suspend fun deleteLeader(leader: Leader): Long

    suspend fun deleteTrainee(trainee: Trainee): Long

    suspend fun deleteAllLeader(): Long

    suspend fun deleteAllTrainee(): Long


    suspend fun getUserType(user_id: Int): Flow<UserType?>

    suspend fun getLeader(leader_id: Int): Leader?

    suspend fun getTrainee(trainee_id: Int): Trainee?

    suspend fun getLeader(user_email: String, user_pass: String): Leader?

    suspend fun getTrainee(user_email: String, user_pass: String): Trainee?

    suspend fun getLeader(leader_email: String): Leader?

    suspend fun getTrainee(trainee_email: String): Trainee?

    suspend fun getAllTrainee(): List<Trainee>

    suspend fun getAllLeader(): List<Leader>

    suspend fun setLinkedTrainee(trainee_id: Int, leader_id: Int): Long

    suspend fun getUnlinkedTrainees(): List<Trainee>

    suspend fun getLinkedTrainees(leader_id: Int): List<Trainee>

    suspend fun setUnlinkedTrainee(trainee_id: Int): Long

    suspend fun updateTraineePosition(trainee_id: Int, trainee_position: Position): Long

    suspend fun updateLeaderName(leaderId: Int, name: String): Long

    suspend fun updateLeaderLastName(leaderId: Int, lastName: String): Long

    suspend fun updateLeaderPassword(leaderId: Int, pass: String): Long
}
