package com.aferrari.trailead.data

import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.common_enum.UserType
import com.aferrari.trailead.data.db.AppDataBase
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.LeaderWithTrainee
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo

class LocalDataSourceImpl(db: AppDataBase) : LocalDataSource {

    private val localDataBase = db.localDataSourceDao

    override suspend fun insertYouTubeVideo(newYouTubeVideo: YouTubeVideo) =
        localDataBase.insertYouTubeVideo(newYouTubeVideo)

    override suspend fun getAllYoutubeVideo(leaderId: Int): List<YouTubeVideo> =
        localDataBase.getAllYoutubeVideo(leaderId)

    override suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo) =
        localDataBase.deleteYoutubeVideo(youTubeVideo)

    override suspend fun updateUrlYoutubeVideo(materialId: Int, youtubeId: String) =
        localDataBase.updateUrlYoutubeVideo(materialId, youtubeId)

    override suspend fun updateTitleYoutubeVideo(youtubeVideoId: Int, newTitle: String) =
        localDataBase.updateTitleYoutubeVideo(youtubeVideoId, newTitle)

    override suspend fun getYoutubeVideoByCategory(
        leaderId: Int,
        categoryId: Int
    ): List<YouTubeVideo> = localDataBase.getYoutubeVideoByCategory(leaderId, categoryId)

    override suspend fun insertCategory(category: Category) = localDataBase.insertCategory(category)

    override suspend fun getAllCategory(leaderId: Int): List<Category> =
        localDataBase.getAllCategory(leaderId)

    override suspend fun deleteCategory(category: Category) = localDataBase.deleteCategory(category)

    override suspend fun updateCategory(categoryId: Int, categoryName: String) =
        localDataBase.updateCategory(categoryId, categoryName)

    override suspend fun insertAllCategoryFromTrainee(traineeCategoryJoin: List<TraineeCategoryJoin>) =
        localDataBase.insertAllCategoryFromTrainee(traineeCategoryJoin)

    override suspend fun getCategoriesFromTrainee(traineeId: Int): List<Category> =
        localDataBase.getCategoriesFromTrainee(traineeId)

    override suspend fun removeAllCategoryFromTrainee(traineeId: Int) =
        localDataBase.removeAllCategoryFromTrainee(traineeId)

    override suspend fun insertLink(link: Link) = localDataBase.insertLink(link)

    override suspend fun deleteLink(link: Link) = localDataBase.deleteLink(link)

    override suspend fun getAllLink(leaderId: Int): List<Link> = localDataBase.getAllLink(leaderId)

    override suspend fun updateUrlLink(linkId: Int, link: String) =
        localDataBase.updateUrlLink(linkId, link)

    override suspend fun updateTitleLink(linkId: Int, newTitle: String) =
        localDataBase.updateTitleLink(linkId, newTitle)

    override suspend fun getLinkByCategory(leaderId: Int, categoryId: Int): List<Link> =
        localDataBase.getLinkByCategory(leaderId, categoryId)

    override suspend fun insertLeader(leader: Leader): Long = localDataBase.insertLeader(leader)

    override suspend fun insertTrainee(trainee: Trainee): Long =
        localDataBase.insertTrainee(trainee)

    override suspend fun updateLeader(leader: Leader) = localDataBase.updateLeader(leader)

    override suspend fun updateTrainee(trainee: Trainee) = localDataBase.updateTrainee(trainee)

    override suspend fun updateTraineeName(idTrainee: Int, name: String) =
        localDataBase.updateTraineeName(idTrainee, name)

    override suspend fun updateTraineeLastName(idTrainee: Int, lastName: String) =
        localDataBase.updateTraineeLastName(idTrainee, lastName)

    override suspend fun updateTraineePassword(password: String, idTrainee: Int) =
        localDataBase.updateTraineePassword(password, idTrainee)

    override suspend fun deleteLeader(leader: Leader) = localDataBase.deleteLeader(leader)

    override suspend fun deleteTrainee(trainee: Trainee) = localDataBase.deleteTrainee(trainee)

    override suspend fun deleteAllLeader(): StatusCode {
        localDataBase.deleteAllLeader()
        return StatusCode.SUCCESS
    }

    override suspend fun deleteAllTrainee(): StatusCode {
        localDataBase.deleteAllTrainee()
        return StatusCode.SUCCESS
    }

    override suspend fun getLeadersWithTrainee(): List<LeaderWithTrainee> =
        localDataBase.getLeadersWithTrainee()

    override suspend fun isEmpty(): Boolean {
        if (localDataBase.leaderCount() > 0 || localDataBase.traineeCount() > 0) {
            return false
        }
        return true
    }

    override suspend fun getUserType(user_id: Int): UserType? {
        val leader = localDataBase.getLeader(user_id)
        if (leader != null) {
            return leader.userType
        }
        val trainee = localDataBase.getTrainee(user_id)
        if (trainee != null) {
            return trainee.userType
        }
        return null
    }

    override suspend fun getLeader(leader_id: Int): Leader? = localDataBase.getLeader(leader_id)

    override suspend fun getLeader(user_email: String, user_pass: String): Leader? =
        localDataBase.getLeader(user_email, user_pass)

    override suspend fun getLeader(leader_email: String): Leader? =
        localDataBase.getLeader(leader_email)


    override suspend fun getTrainee(trainee_id: Int): Trainee? =
        localDataBase.getTrainee(trainee_id)

    override suspend fun getTrainee(user_email: String, user_pass: String): Trainee? =
        localDataBase.getTrainee(user_email, user_pass)

    override suspend fun getTrainee(trainee_email: String): Trainee? =
        localDataBase.getTrainee(trainee_email)

    override suspend fun getAllTrainee(): List<Trainee> =
        localDataBase.getAllTrainee()

    override suspend fun getAllLeader(): List<Leader> =
        localDataBase.getAllLeader()

    override suspend fun setLinkedTrainee(trainee_id: Int, leader_id: Int): StatusCode {
        localDataBase.setLinkedTrainee(trainee_id, leader_id)
        return StatusCode.SUCCESS
    }

    override suspend fun getUnlinkedTrainees(): List<Trainee> = localDataBase.getUnlinkedTrainees()

    override suspend fun getLinkedTrainees(leader_id: Int): List<Trainee> =
        localDataBase.getLinkedTrainees(leader_id)

    override suspend fun setUnlinkedTrainee(trainee_id: Int): StatusCode {
        localDataBase.setUnlinkedTrainee(trainee_id)
        return StatusCode.SUCCESS
    }

    override suspend fun updateTraineePosition(
        trainee_id: Int,
        trainee_position: Position
    ): StatusCode {
        localDataBase.updateTraineePosition(trainee_id, trainee_position)
        return StatusCode.SUCCESS
    }

    override suspend fun updateLeaderName(leaderId: Int, name: String): StatusCode {
        localDataBase.updateLeaderName(leaderId, name)
        return StatusCode.SUCCESS
    }

    override suspend fun updateLeaderLastName(leaderId: Int, lastName: String): StatusCode {
        localDataBase.updateLeaderLastName(leaderId, lastName)
        return StatusCode.SUCCESS
    }

    override suspend fun updateLeaderPassword(leaderId: Int, pass: String): StatusCode {
        localDataBase.updateLeaderPassword(leaderId, pass)
        return StatusCode.SUCCESS
    }
}