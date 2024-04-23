package com.aferrari.trailead.domain.repository

import com.aferrari.trailead.common.IntegerUtils
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MaterialRepository(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun initLocalDataSource() =
        withContext(Dispatchers.IO) {
            val categoryList = remoteDataSource.getAllCategory()
            categoryList.forEach {
//                localDataSource.insertCategory(it)
            }
            val linkList = remoteDataSource.getAllLink()
            linkList.forEach {
//                localDataSource.insertLink(it)
            }
            val youtubeVideoList = remoteDataSource.getAllYoutubeVideo()
            youtubeVideoList.forEach {
//                localDataSource.insertYouTubeVideo(it)
            }
            val traineeCategoryJoinList = remoteDataSource.getAllTraineeCategoryJoin()
//            localDataSource.insertAllTraineeCategoryJoin(traineeCategoryJoinList)
        }

    //  Video
    suspend fun insertYoutubeVideo(newYouTubeVideo: YouTubeVideo) {
        val result = remoteDataSource.insertYouTubeVideo(newYouTubeVideo)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.insertYouTubeVideo(newYouTubeVideo)
        }
    }

    suspend fun getAllYoutubeVideo(leader: Leader) = remoteDataSource.getAllYoutubeVideo(leader.userId)

    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo) {
        val result = remoteDataSource.deleteYoutubeVideo(youTubeVideo)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.deleteYoutubeVideo(youTubeVideo)
        }
    }

    suspend fun updateUrlYoutubeVideo(youtubeId: Int, youtubeUrl: String) {
        val result = remoteDataSource.updateUrlYoutubeVideo(youtubeId, youtubeUrl)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateUrlYoutubeVideo(youtubeId, youtubeUrl)
        }
    }


    suspend fun updateTitleYoutubeVideo(youtubeId: Int, newTitle: String) {
        val result = remoteDataSource.updateTitleYoutubeVideo(youtubeId, newTitle)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateTitleYoutubeVideo(youtubeId, newTitle)
        }
    }

    suspend fun getYoutubeVideoByCategory(leaderId: String, categoryId: Int) =
        remoteDataSource.getYoutubeVideoByCategory(leaderId, categoryId)

    //  Link
    suspend fun insertLink(link: Link) {
        val result = remoteDataSource.insertLink(link)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.insertLink(link)
        }
    }

    suspend fun updateTitleLink(linkId: Int, newTitle: String) {
        val result = remoteDataSource.updateTitleLink(linkId, newTitle)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateTitleLink(linkId, newTitle)
        }
    }

    suspend fun updateUrlLink(linkId: Int, newUrl: String) {
        val result = remoteDataSource.updateUrlLink(linkId, newUrl)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateUrlLink(linkId, newUrl)
        }
    }

    suspend fun getAllLink(leader: Leader) = remoteDataSource.getAllLink(leader.userId)

    suspend fun getLinksByCategory(leaderId: String, categoryId: Int) =
        remoteDataSource.getLinkByCategory(leaderId, categoryId)

    suspend fun deleteLink(link: Link) {
        val result = remoteDataSource.deleteLink(link)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.deleteLink(link)
        }
    }

    //  Category
    suspend fun insertCategory(category: Category) {
        val result = remoteDataSource.insertCategory(category)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.insertCategory(category)
        }
    }

    suspend fun getAllCategory(leader: Leader) = remoteDataSource.getAllCategory(leader.userId)

    suspend fun deleteCategory(category: Category) {
        var result = remoteDataSource.deleteAllTraineeCategoryJoinForCategory(category.id)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.deleteAllTraineeCategoryJoinForCategory(category.id)
            result = remoteDataSource.deleteCategory(category)
            if (result == StatusCode.SUCCESS.value) {
//                localDataSource.deleteCategory(category)
            }
        }
    }

    suspend fun updateCategory(categoryId: Int, categoryName: String) {
        val result = remoteDataSource.updateCategory(categoryId, categoryName)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.updateCategory(categoryId, categoryName)
        }
    }

    suspend fun setLinkedCategory(traineeId: String, categorySet: MutableSet<Category>): Long {
        val traineeCategoryJoinList = categorySet.map {
            TraineeCategoryJoin(
                IntegerUtils().createObjectId(),
                traineeId,
                it.id
            )
        }
        var result = remoteDataSource.deleteAllTraineeCategoryJoinForTrainee(traineeId)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.deleteAllTraineeCategoryJoinForTrainee(traineeId)
        } else {
            return StatusCode.ERROR.value
        }
        result = remoteDataSource.insertAllCategoryFromTrainee(traineeCategoryJoinList)
        if (result == StatusCode.SUCCESS.value) {
//            localDataSource.insertAllTraineeCategoryJoin(traineeCategoryJoinList)
        } else {
            return StatusCode.ERROR.value
        }
        return StatusCode.SUCCESS.value
    }

    suspend fun getCategoriesSelected(traineeId: String): List<Category> =
        remoteDataSource.getCategoriesFromTrainee(traineeId)
}