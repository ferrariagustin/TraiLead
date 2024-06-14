package com.aferrari.trailead.domain.repository

import android.net.Uri
import com.aferrari.trailead.app.configurer.NetworkManager
import com.aferrari.trailead.common.IntegerUtils
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Pdf
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo
import java.io.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class MaterialRepository(private val remoteDataSource: RemoteDataSource) {

    //  Video
    suspend fun insertYoutubeVideo(newYouTubeVideo: YouTubeVideo) =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION.value
        } else {
            remoteDataSource.insertYouTubeVideo(newYouTubeVideo)
        }

    suspend fun getAllYoutubeVideo(leader: Leader) =
        remoteDataSource.getAllYoutubeVideo(leader.userId)

    suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo) =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION.value
        } else {
            remoteDataSource.deleteYoutubeVideo(youTubeVideo)
        }


    suspend fun updateUrlYoutubeVideo(youtubeId: Int, youtubeUrl: String) =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION.value
        } else {
            remoteDataSource.updateUrlYoutubeVideo(youtubeId, youtubeUrl)
        }

    suspend fun updateTitleYoutubeVideo(youtubeId: Int, newTitle: String) =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION.value
        } else {
            remoteDataSource.updateTitleYoutubeVideo(youtubeId, newTitle)
        }

    suspend fun getYoutubeVideoByCategory(leaderId: String, categoryId: Int) =
        remoteDataSource.getYoutubeVideoByCategory(leaderId, categoryId)

    //  Link
    suspend fun insertLink(link: Link) =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION.value
        } else {
            remoteDataSource.insertLink(link)
        }

    suspend fun updateTitleLink(linkId: Int, newTitle: String) =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION.value
        } else {
            remoteDataSource.updateTitleLink(linkId, newTitle)
        }

    suspend fun updateUrlLink(linkId: Int, newUrl: String) =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION.value
        } else {
            remoteDataSource.updateUrlLink(linkId, newUrl)
        }

    suspend fun getAllLink(leader: Leader) = remoteDataSource.getAllLink(leader.userId)

    suspend fun getLinksByCategory(leaderId: String, categoryId: Int) =
        remoteDataSource.getLinksByCategory(leaderId, categoryId)

    suspend fun deleteLink(link: Link) = if (!NetworkManager.isOnline()) {
        StatusCode.INTERNET_CONECTION.value
    } else {
        remoteDataSource.deleteLink(link)
    }

    //  Category
    suspend fun insertCategory(category: Category): Flow<StatusCode> =
        if (!NetworkManager.isOnline()) {
            flowOf(StatusCode.INTERNET_CONECTION)
        } else {
            remoteDataSource.insertCategory(category)
        }

    suspend fun getAllCategory(leader: Leader) = remoteDataSource.getAllCategory(leader.userId)

    suspend fun deleteCategory(idCategory: Int) =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION.value
        } else {
            remoteDataSource.deleteAllTraineeCategoryJoinForCategory(idCategory)
        }

    suspend fun updateCategory(categoryId: Int, categoryName: String) =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION.value
        } else {
            remoteDataSource.updateCategory(categoryId, categoryName)
        }

    suspend fun setLinkedCategory(traineeId: String, categorySet: MutableSet<Category>): Long =
        if (!NetworkManager.isOnline()) {
            StatusCode.INTERNET_CONECTION.value
        } else {
            val traineeCategoryJoinList = categorySet.map {
                TraineeCategoryJoin(
                    IntegerUtils().createObjectId(),
                    traineeId,
                    it.id
                )
            }
            if (remoteDataSource.deleteAllTraineeCategoryJoinForTrainee(traineeId) == StatusCode.SUCCESS.value)
                remoteDataSource.insertAllCategoryFromTrainee(traineeCategoryJoinList)
            else
                StatusCode.ERROR.value
        }

    suspend fun getCategoriesSelected(traineeId: String): List<Category> =
        remoteDataSource.getCategoriesFromTrainee(traineeId)

    // PDF

    suspend fun insertPDF(
        pdfTitle: String,
        uri: Uri,
        categoryId: Int,
        leaderId: String
    ) = flow<StatusCode> {
        if (!NetworkManager.isOnline()) {
            emit(StatusCode.INTERNET_CONECTION)
        } else {
            remoteDataSource.insertPDF(
                Pdf(
                    IntegerUtils().createObjectId(),
                    pdfTitle,
                    uri.toString(),
                    categoryId,
                    leaderId
                )
            ).collect {
                emit(it)
            }
        }
    }

    suspend fun getAllPDF(leader: Leader) =
        remoteDataSource.getAllPDF(leader.userId)

    fun getPdf(pdf: Pdf) = flow<File?> {
        if (!NetworkManager.isOnline()) {
            emit(null)
        } else {
            emit(remoteDataSource.getPdf(pdf))
        }
    }

    /**
     * Delete Pdf from database and storage
     */
    suspend fun deletePdf(pdf: Pdf): Long =
        if (!NetworkManager.isOnline())
            StatusCode.INTERNET_CONECTION.value
        else remoteDataSource.deletePdf(pdf)
}
