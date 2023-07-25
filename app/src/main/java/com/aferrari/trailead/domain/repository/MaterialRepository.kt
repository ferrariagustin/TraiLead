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
    suspend fun insertYoutubeVideo(newYouTubeVideo: YouTubeVideo) {
        val result = remoteDataSource.insertYouTubeVideo(newYouTubeVideo)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.insertYouTubeVideo(newYouTubeVideo)
        }
    }

    suspend fun getAllYoutubeVideo(leader: Leader) = remoteDataSource.getAllYoutubeVideo(leader.id)

    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo) =
        localDataSource.deleteYoutubeVideo(youTubeVideo)

    suspend fun updateUrlYoutubeVideo(youtubeId: Int, youtubeUrl: String) {
        val result = remoteDataSource.updateUrlYoutubeVideo(youtubeId, youtubeUrl)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateUrlYoutubeVideo(youtubeId, youtubeUrl)
        }
    }


    suspend fun updateTitleYoutubeVideo(youtubeId: Int, newTitle: String) {
        val result = remoteDataSource.updateTitleYoutubeVideo(youtubeId, newTitle)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateTitleYoutubeVideo(youtubeId, newTitle)
        }
    }

    suspend fun getYoutubeVideoByCategory(leaderId: Int, categoryId: Int) =
        localDataSource.getYoutubeVideoByCategory(leaderId, categoryId)

    //  Link
    suspend fun insertLink(link: Link) {
        val result = remoteDataSource.insertLink(link)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.insertLink(link)
        }
    }

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

    suspend fun getAllLink(leader: Leader) = remoteDataSource.getAllLink(leader.id)

    suspend fun getLinksByCategory(leaderId: Int, categoryId: Int) =
        localDataSource.getLinkByCategory(leaderId, categoryId)

    suspend fun deleteLink(link: Link) = localDataSource.deleteLink(link)

    //  Category
    suspend fun insertCategory(category: Category) {
        val result = remoteDataSource.insertCategory(category)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.insertCategory(category)
        }
    }

    suspend fun getAllCategory(leader: Leader) = remoteDataSource.getAllCategory(leader.id)

    suspend fun deleteCategory(category: Category) = localDataSource.deleteCategory(category)

    suspend fun updateCategory(categoryId: Int, categoryName: String) {
        val result = remoteDataSource.updateCategory(categoryId, categoryName)
        if (result == StatusCode.SUCCESS.value) {
            localDataSource.updateCategory(categoryId, categoryName)
        }
    }

    suspend fun setLinkedCategory(traineeId: Int, categorySet: MutableSet<Category>) {
        val traineeCategoryJoinList = categorySet.map { TraineeCategoryJoin(traineeId, it.id) }
        localDataSource.removeAllCategoryFromTrainee(traineeId)
        return localDataSource.insertAllCategoryFromTrainee(traineeCategoryJoinList)
    }

    suspend fun getCategoriesSelected(traineeId: Int): List<Category> =
        localDataSource.getCategoriesFromTrainee(traineeId)
}