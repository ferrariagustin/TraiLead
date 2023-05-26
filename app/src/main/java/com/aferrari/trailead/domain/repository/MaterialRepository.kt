package com.aferrari.trailead.domain.repository

import com.aferrari.trailead.data.apiservices.MaterialDao
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo

class MaterialRepository(private val dao: MaterialDao) {
    //  Video
    suspend fun insertYoutubeVideo(newYouTubeVideo: YouTubeVideo) =
        dao.insertYouTubeVideo(newYouTubeVideo)

    suspend fun getAllYoutubeVideo(leader: Leader) = dao.getAllYoutubeVideo(leader.id)

    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo) =
        dao.deleteYoutubeVideo(youTubeVideo)

    suspend fun updateUrlYoutubeVideo(materialId: Int, youtubeId: String) =
        dao.updateUrlYoutubeVideo(materialId, youtubeId)

    suspend fun updateTitleYoutubeVideo(materialId: Int, newTitle: String) =
        dao.updateTitleYoutubeVideo(materialId, newTitle)

    suspend fun getYoutubeVideoByCategory(leaderId: Int, categoryId: Int) =
        dao.getYoutubeVideoByCategory(leaderId, categoryId)

    //  Link
    suspend fun insertLink(link: Link) = dao.insertLink(link)

    suspend fun updateTitleLink(linkId: Int, newTitle: String) =
        dao.updateTitleLink(linkId, newTitle)

    suspend fun updateUrlLink(linkId: Int, newUrl: String) = dao.updateUrlLink(linkId, newUrl)

    suspend fun getAllLink(leader: Leader) = dao.getAllLink(leader.id)

    suspend fun getLinksByCategory(leaderId: Int, categoryId: Int) =
        dao.getLinkByCategory(leaderId, categoryId)

    suspend fun deleteLink(link: Link) = dao.deleteLink(link)

    //  Category
    suspend fun insertCategory(category: Category) = dao.insertCategory(category)

    suspend fun getAllCategory(leader: Leader) = dao.getAllCategory(leader.id)

    suspend fun deleteCategory(category: Category) = dao.deleteCategory(category)

    suspend fun updateCategory(categoryId: Int, categoryName: String) =
        dao.updateCategory(categoryId, categoryName)

    suspend fun setLinkedCategory(traineeId: Int, categorySet: MutableSet<Category>) {
        val traineeCategoryJoinList = categorySet.map { TraineeCategoryJoin(traineeId, it.id) }
        dao.removeAllCategoryFromTrainee(traineeId)
        return dao.insertAllCategoryFromTrainee(traineeCategoryJoinList)
    }

    suspend fun getCategoriesSelected(traineeId: Int): List<Category> =
        dao.getCategoriesFromTrainee(traineeId)
}