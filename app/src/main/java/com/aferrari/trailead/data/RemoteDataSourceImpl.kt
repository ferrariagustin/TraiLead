package com.aferrari.trailead.data

import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.LeaderWithTrainee
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor() : RemoteDataSource {
    override suspend fun insertYouTubeVideo(newYouTubeVideo: YouTubeVideo) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllYoutubeVideo(leaderId: Int): List<YouTubeVideo> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUrlYoutubeVideo(materialId: Int, youtubeId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTitleYoutubeVideo(youtubeVideoId: Int, newTitle: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getYoutubeVideoByCategory(
        leaderId: Int,
        categoryId: Int
    ): List<YouTubeVideo> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCategory(leaderId: Int): List<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCategory(categoryId: Int, categoryName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllCategoryFromTrainee(traineeCategoryJoin: List<TraineeCategoryJoin>) {
        TODO("Not yet implemented")
    }

    override suspend fun getCategoriesFromTrainee(traineeId: Int): List<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllCategoryFromTrainee(traineeId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun insertLink(link: Link) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLink(link: Link) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLink(leaderId: Int): List<Link> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUrlLink(linkId: Int, link: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTitleLink(linkId: Int, newTitle: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getLinkByCategory(leaderId: Int, categoryId: Int): List<Link> {
        TODO("Not yet implemented")
    }

    override suspend fun insertLeader(leader: Leader): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertTrainee(trainee: Trainee): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateLeader(leader: Leader) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTrainee(trainee: Trainee) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTraineeName(idTrainee: Int, name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTraineeLastName(idTrainee: Int, lastName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTraineePassword(password: String, idTrainee: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLeader(leader: Leader) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTrainee(trainee: Trainee) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllLeader() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTrainee() {
        TODO("Not yet implemented")
    }

    override suspend fun getLeadersWithTrainee(): List<LeaderWithTrainee> {
        TODO("Not yet implemented")
    }

    override suspend fun getLeader(leader_id: Int): Leader? {
        TODO("Not yet implemented")
    }

    override suspend fun getLeader(user_email: String, user_pass: String): Leader? {
        TODO("Not yet implemented")
    }

    override suspend fun getLeader(leader_email: String): Leader? {
        TODO("Not yet implemented")
    }

    override suspend fun getTrainee(trainee_id: Int): Trainee? {
        TODO("Not yet implemented")
    }

    override suspend fun getTrainee(user_email: String, user_pass: String): Trainee? {
        TODO("Not yet implemented")
    }

    override suspend fun getTrainee(trainee_email: String): Trainee? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTrainee(): List<Trainee> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLeader(): List<Leader> {
        TODO("Not yet implemented")
    }

    override suspend fun setLinkedTrainee(trainee_id: Int, leader_id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getUnlinkedTrainees(): List<Trainee> {
        TODO("Not yet implemented")
    }

    override suspend fun getLinkedTrainees(leader_id: Int): List<Trainee> {
        TODO("Not yet implemented")
    }

    override suspend fun setUnlinkedTrainee(trainee_id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTraineePosition(trainee_id: Int, trainee_position: Position) {
        TODO("Not yet implemented")
    }

    override suspend fun updateLeaderName(leaderId: Int, name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateLeaderLastName(leaderId: Int, lastName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateLeaderPassword(leaderId: Int, pass: String) {
        TODO("Not yet implemented")
    }
}