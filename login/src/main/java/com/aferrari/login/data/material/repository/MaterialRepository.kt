package com.aferrari.login.data.material.repository

import com.aferrari.login.data.material.Category
import com.aferrari.login.data.user.Leader
import com.aferrari.login.data.TraineeCategoryJoin
import com.aferrari.login.data.material.YouTubeVideo
import com.aferrari.login.data.material.dao.MaterialDao

class MaterialRepository(private val dao: MaterialDao) {
    //    Material
    suspend fun insertYoutubeVideo(newYouTubeVideo: YouTubeVideo) =
        dao.insertYouTubeVideo(newYouTubeVideo)

    suspend fun getAllYoutubeVideo(leader: Leader) = dao.getAllYoutubeVideo(leader.id)

    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo) =
        dao.deleteYoutubeVideo(youTubeVideo)

    suspend fun updateUrlYoutubeVideo(materialId: Int, youtubeId: String) =
        dao.updateUrlYoutubeVideo(materialId, youtubeId)

    suspend fun updateTitleYoutubeVideo(materialId: Int, newTitle: String) =
        dao.updateTitleYoutubeVideo(materialId, newTitle)

    suspend fun getYoutubeVideoByTrainee(leaderId: Int, categoryId: Int) =
        dao.getYoutubeVideoByCategory(leaderId, categoryId)

    //    Category
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