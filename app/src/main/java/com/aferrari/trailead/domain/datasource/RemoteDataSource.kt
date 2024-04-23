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

    suspend fun getAllYoutubeVideo(leaderId: String): List<YouTubeVideo>

    suspend fun getAllYoutubeVideo(): List<YouTubeVideo>

    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo): Long

    suspend fun updateUrlYoutubeVideo(youtubeId: Int, youtubeUrl: String): Long

    suspend fun updateTitleYoutubeVideo(youtubeId: Int, newTitle: String): Long

    suspend fun getYoutubeVideoByCategory(leaderId: String, categoryId: Int): List<YouTubeVideo>

    //    Category

    suspend fun insertCategory(category: Category): Long

    suspend fun getAllCategory(leaderId: String): List<Category>

    suspend fun getAllCategory(): List<Category>

    suspend fun deleteCategory(category: Category): Long

    suspend fun updateCategory(categoryId: Int, categoryName: String): Long

    suspend fun insertAllCategoryFromTrainee(traineeCategoryJoinList: List<TraineeCategoryJoin>): Long

    suspend fun insertCategoryFromTrainee(traineeCategoryJoin: TraineeCategoryJoin): Long

    suspend fun getCategoriesFromTrainee(traineeId: String): List<Category>

    suspend fun getCategory(categoryId: Int): Category?

    suspend fun deleteAllTraineeCategoryJoinForTrainee(traineeId: String): Long

    suspend fun deleteAllTraineeCategoryJoinForCategory(categoryId: Int): Long

    suspend fun getAllTraineeCategoryJoin(): List<TraineeCategoryJoin>

    suspend fun deleteTraineeCategoryJoin(traineeCategoryJoinId: Int): Long

    //  Link
    suspend fun insertLink(link: Link): Long

    suspend fun deleteLink(link: Link): Long

    suspend fun getAllLink(leaderId: String): List<Link>

    suspend fun getAllLink(): List<Link>

    suspend fun updateUrlLink(linkId: Int, link: String): Long

    suspend fun updateTitleLink(linkId: Int, newTitle: String): Long

    suspend fun getLinkByCategory(leaderId: String, categoryId: Int): List<Link>

    suspend fun insertLeader(leader: Leader): Long

    suspend fun insertTrainee(trainee: Trainee): Long

    suspend fun updateTraineeName(idTrainee: String, name: String): Long

    suspend fun updateTraineeLastName(idTrainee: String, lastName: String): Long

    suspend fun updateTraineePassword(idTrainee: String, pass: String): Long

    suspend fun updateUserPassword(pass: String): Long

    suspend fun deleteLeader(leader: Leader): Long

    suspend fun deleteTrainee(trainee: Trainee): Long

    suspend fun deleteAllLeader(): Long

    suspend fun deleteAllTrainee(): Long


    suspend fun getUserType(userId: String): Flow<UserType?>

    suspend fun getLeader(leaderId: String): Leader?

    suspend fun getTrainee(traineeId: String): Trainee?

//    suspend fun getLeader(userEmail: String, userPass: String): Leader?

//    suspend fun getTrainee(userEmail: String, userPass: String): Trainee?

    suspend fun getLeaderByEmail(leaderEmail: String): Leader?

    suspend fun getTraineeByEmail(traineeEmail: String): Trainee?

    suspend fun getAllTrainee(): List<Trainee>

    suspend fun getAllLeader(): List<Leader>

    suspend fun setLinkedTrainee(traineeId: String, leaderId: String): Long

    suspend fun getUnlinkedTrainees(): List<Trainee>

    suspend fun getLinkedTrainees(leaderId: String): List<Trainee>

    suspend fun setUnlinkedTrainee(traineeId: String): Long

    suspend fun updateTraineePosition(traineeId: String, trainee_position: Position): Long

    suspend fun updateLeaderName(leaderId: String, name: String): Long

    suspend fun updateLeaderLastName(leaderId: String, lastName: String): Long

//    suspend fun updateLeaderPassword(leaderId: String, pass: String): Long
    suspend fun validateLeaderAccessKey(leaderId: String, accessKey: Int): Long
    suspend fun validateTraineeAccessKey(traineeId: String, accessKey: Int): Long
    suspend fun updateLeaderAccessKey(leaderId: String, accessKey: Int): Long
    suspend fun updateTraineeAccessKey(traineeIds: String, accessKey: Int): Long
}
