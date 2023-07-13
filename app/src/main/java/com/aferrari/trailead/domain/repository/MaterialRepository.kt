package com.aferrari.trailead.domain.repository

import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo

class MaterialRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    //  Video
    suspend fun insertYoutubeVideo(newYouTubeVideo: YouTubeVideo) =
        localDataSource.insertYouTubeVideo(newYouTubeVideo)

    suspend fun getAllYoutubeVideo(leader: Leader) = localDataSource.getAllYoutubeVideo(leader.id)

    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo) =
        localDataSource.deleteYoutubeVideo(youTubeVideo)

    suspend fun updateUrlYoutubeVideo(materialId: Int, youtubeId: String) =
        localDataSource.updateUrlYoutubeVideo(materialId, youtubeId)

    suspend fun updateTitleYoutubeVideo(materialId: Int, newTitle: String) =
        localDataSource.updateTitleYoutubeVideo(materialId, newTitle)

    suspend fun getYoutubeVideoByCategory(leaderId: Int, categoryId: Int) =
        localDataSource.getYoutubeVideoByCategory(leaderId, categoryId)

    //  Link
    suspend fun insertLink(link: Link) = localDataSource.insertLink(link)

    suspend fun updateTitleLink(linkId: Int, newTitle: String) {
        val result = remoteDataSource.updateTitleLink(linkId, newTitle)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateTitleLink(linkId, newTitle)
        }
    }

    suspend fun updateUrlLink(linkId: Int, newUrl: String) {
        val result = remoteDataSource.updateUrlLink(linkId, newUrl)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateUrlLink(linkId, newUrl)
        }
    }

    suspend fun getAllLink(leader: Leader) = localDataSource.getAllLink(leader.id)

    suspend fun getLinksByCategory(leaderId: Int, categoryId: Int) =
        localDataSource.getLinkByCategory(leaderId, categoryId)

    suspend fun deleteLink(link: Link) = localDataSource.deleteLink(link)

    //  Category
    suspend fun insertCategory(category: Category) = localDataSource.insertCategory(category)

    suspend fun getAllCategory(leader: Leader) = localDataSource.getAllCategory(leader.id)

    suspend fun deleteCategory(category: Category) = localDataSource.deleteCategory(category)

    suspend fun updateCategory(categoryId: Int, categoryName: String) =
        localDataSource.updateCategory(categoryId, categoryName)

    suspend fun setLinkedCategory(traineeId: Int, categorySet: MutableSet<Category>) {
        val traineeCategoryJoinList = categorySet.map { TraineeCategoryJoin(traineeId, it.id) }
        localDataSource.removeAllCategoryFromTrainee(traineeId)
        return localDataSource.insertAllCategoryFromTrainee(traineeCategoryJoinList)
    }

    suspend fun getCategoriesSelected(traineeId: Int): List<Category> =
        localDataSource.getCategoriesFromTrainee(traineeId)
}