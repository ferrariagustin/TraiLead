package com.aferrari.trailead.data

import android.net.Uri
import android.util.Log
import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.common_enum.UserType
import com.aferrari.trailead.data.db.FirebaseDataBase
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Pdf
import com.aferrari.trailead.domain.models.Token
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo
import com.aferrari.trailead.notification.api.FcmAPI
import com.aferrari.trailead.notification.model.FcmResponse
import com.aferrari.trailead.notification.model.NotificationBody
import com.aferrari.trailead.notification.model.SendMessageDto
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import java.io.File
import java.util.concurrent.CompletableFuture
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.future.await
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RemoteDataSourceImpl @Inject constructor(private val fcmAPI: FcmAPI) : RemoteDataSource {

    // PDF
    override suspend fun insertPDF(pdf: Pdf) = flow {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("pdfs")
        val result = CompletableFuture<StatusCode>()
        try {
            Uri.parse(pdf.url)?.let {
                storageRef.child("${pdf.id}/").putFile(it)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            pdf.downloadUri = task.result.uploadSessionUri.toString()
                            result.complete(StatusCode.SUCCESS)
                        } else {
                            result.complete(StatusCode.ERROR)
                        }
                    }.await()
                if (result.await() == StatusCode.SUCCESS) {
                    emit(insertPDFDatabase(pdf))
                } else {
                    emit(StatusCode.ERROR)
                }
            }
        } catch (e: Exception) {
            emit(StatusCode.ERROR)
        }
    }

    override suspend fun getAllPDF(leaderId: String): List<Pdf> = withContext(Dispatchers.IO) {
        getAllPDF().filter { it.leaderMaterialId == leaderId }
    }

    override suspend fun getAllPDF(): List<Pdf> =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Pdf::class.simpleName.toString())
            val dataSnapshot = reference?.get()?.await()
            val pdfList = mutableListOf<Pdf>()
            if (dataSnapshot?.key == Pdf::class.simpleName.toString()) {
                dataSnapshot.value?.let {
                    val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                    pdfList.addAll(hashMapValues.values.map {
                        Gson().fromJson(Gson().toJson(it), Pdf::class.java)
                    })
                }
            }
            pdfList
        }

    override suspend fun getPdf(pdf: Pdf): File? {
        val storageRef = FirebaseStorage.getInstance().reference.child("pdfs")
        val httpReference =
            storageRef.child("${pdf.id}/")
        val localFile = File.createTempFile("traileadPdf", "pdf")
        val result = CompletableFuture<StatusCode>()
        try {
            httpReference.getFile(localFile).addOnCompleteListener {
                if (it.isSuccessful) {
                    result.complete(StatusCode.SUCCESS)
                } else {
                    result.complete(StatusCode.ERROR)
                }
            }.await()
        } catch (_: Exception) {
            result.complete(StatusCode.ERROR)
        }
        delay(3000)
        return if (result.await() == StatusCode.SUCCESS) {
            localFile
        } else {
            null
        }
    }

    override suspend fun deletePdf(pdf: Pdf): Long =
        withContext(Dispatchers.IO) {
            val resultDeletePdfStorage = deletePdfStorage(pdf)
            if (resultDeletePdfStorage == StatusCode.ERROR) {
                return@withContext StatusCode.ERROR.value
            }
            val reference =
                FirebaseDataBase.database?.child(Pdf::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(pdf.id.toString())
                ?.removeValue()
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun updatePdf(pdfId: Int, newPdfTitle: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Pdf::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(pdfId.toString())?.child(Pdf::title.name)
                ?.setValue(newPdfTitle)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun getPdfByCategory(leaderId: String, categoryId: Int): List<Pdf> =
        withContext(Dispatchers.IO) {
            val reference = FirebaseDataBase.database?.child(Pdf::class.simpleName.toString())
            val dataSnapshot = reference?.get()?.await()
            val links = mutableListOf<Pdf>()
            if (dataSnapshot?.key == Pdf::class.simpleName.toString()) {
                dataSnapshot.value?.let {
                    val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                    links.addAll(hashMapValues.values.map {
                        Gson().fromJson(Gson().toJson(it), Pdf::class.java)
                    }.filter { it.leaderMaterialId == leaderId && it.categoryId == categoryId })
                }
            }
            links
        }

    private suspend fun deletePdfStorage(pdf: Pdf): StatusCode = withContext(Dispatchers.IO) {
        val storageRef = FirebaseStorage.getInstance().reference.child("pdfs")
        val httpReference = storageRef.child("${pdf.id}/")
        val result = CompletableFuture<StatusCode>()
        try {
            httpReference.delete().addOnCompleteListener {
                result.complete(
                    if (it.isSuccessful) {
                        StatusCode.SUCCESS
                    } else {
                        StatusCode.ERROR
                    }
                )
            }.await()
        } catch (_: Exception) {
            result.complete(StatusCode.ERROR)
        }
        result.await()
    }

    private suspend fun insertPDFDatabase(pdf: Pdf): StatusCode = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Pdf::class.simpleName.toString())
        val result = CompletableFuture<StatusCode>()
        try {
            reference?.child(pdf.id.toString())?.setValue(pdf)?.addOnCompleteListener { task ->
                result.complete(
                    if (task.isSuccessful) {
                        StatusCode.SUCCESS
                    } else {
                        StatusCode.ERROR
                    }
                )
            }?.await()
        } catch (_: Exception) {
            result.complete(StatusCode.ERROR)
        }
        result.await()
    }

    override suspend fun insertYouTubeVideo(newYouTubeVideo: YouTubeVideo): Long =
        withContext(Dispatchers.IO) {
            val result = CompletableFuture<Long>()
            val reference =
                FirebaseDataBase.database?.child(YouTubeVideo::class.simpleName.toString())
            reference?.child(newYouTubeVideo.id.toString())?.setValue(newYouTubeVideo)
                ?.addOnCompleteListener { task ->
                    result.complete(
                        if (task.isSuccessful) {
                            StatusCode.SUCCESS.value
                        } else {
                            StatusCode.ERROR.value
                        }
                    )
                }?.await()
            result.await()
        }

    override suspend fun getAllYoutubeVideo(leaderId: String): List<YouTubeVideo> =
        withContext(Dispatchers.IO) {
            getAllYoutubeVideo().filter { it.leaderMaterialId == leaderId }
        }

    override suspend fun getAllYoutubeVideo(): List<YouTubeVideo> =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(YouTubeVideo::class.simpleName.toString())
            val dataSnapshot = reference?.get()?.await()
            val youtubeVideosList = mutableListOf<YouTubeVideo>()
            if (dataSnapshot?.key == YouTubeVideo::class.simpleName.toString()) {
                dataSnapshot.value?.let {
                    val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                    youtubeVideosList.addAll(hashMapValues.values.map {
                        Gson().fromJson(Gson().toJson(it), YouTubeVideo::class.java)
                    })
                }
            }
            youtubeVideosList
        }

    override suspend fun deleteYoutubeVideo(youTubeVideo: YouTubeVideo): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(YouTubeVideo::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(youTubeVideo.id.toString())
                ?.removeValue()
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun updateUrlYoutubeVideo(youtubeId: Int, youtubeUrl: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(YouTubeVideo::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(youtubeId.toString())?.child(YouTubeVideo::url.name)
                ?.setValue(youtubeUrl)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun updateTitleYoutubeVideo(youtubeId: Int, newTitle: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(YouTubeVideo::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(youtubeId.toString())?.child(YouTubeVideo::title.name)
                ?.setValue(newTitle)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun getYoutubeVideoByCategory(
        leaderId: String,
        categoryId: Int
    ): List<YouTubeVideo> = withContext(Dispatchers.IO) {
        getAllYoutubeVideo().filter { it.categoryId == categoryId }
    }

    override suspend fun insertCategory(category: Category): Flow<StatusCode> = flow {
        val reference = FirebaseDataBase.database?.child(Category::class.simpleName.toString())
        var resultCode = StatusCode.ERROR
        try {
            reference?.child(category.id.toString())?.setValue(category)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS
                    } else {
                        StatusCode.ERROR
                    }
                }?.await()
            delay(500)
            emit(resultCode)
        } catch (exception: Exception) {
            emit(StatusCode.ERROR)
        }
    }

    override suspend fun getAllCategory(leaderId: String): List<Category> =
        getAllCategory().filter { it.leaderCategoryId == leaderId }

    override suspend fun getAllCategory(): List<Category> = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Category::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        var categories = mutableListOf<Category>()
        if (dataSnapshot?.key == Category::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                categories.addAll(hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Category::class.java)
                })
            }
        }
        categories
    }

    override suspend fun deleteCategory(idCategory: Int): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Category::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(idCategory.toString())
                ?.removeValue()
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            delay(500)
            resultCode
        }

    override suspend fun updateCategory(categoryId: Int, categoryName: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Category::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(categoryId.toString())?.child(Category::name.name)
                ?.setValue(categoryName)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun insertAllCategoryFromTrainee(traineeCategoryJoinList: List<TraineeCategoryJoin>): Long =
        withContext(Dispatchers.IO) {
            traineeCategoryJoinList.forEach {
                val result = insertCategoryFromTrainee(it)
                if (result == StatusCode.ERROR.value) {
                    return@withContext StatusCode.ERROR.value
                }
            }
            return@withContext StatusCode.SUCCESS.value
        }

    override suspend fun insertCategoryFromTrainee(traineeCategoryJoin: TraineeCategoryJoin): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(TraineeCategoryJoin::class.simpleName.toString())
            val result = CompletableFuture<StatusCode>()
            reference?.child(traineeCategoryJoin.id.toString())?.setValue(traineeCategoryJoin)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.complete(StatusCode.SUCCESS)
                    } else {
                        result.complete(StatusCode.ERROR)
                    }
                }?.await()
            return@withContext result.await().value
        }

    override suspend fun getCategoriesFromTrainee(traineeId: String): List<Category> =
        withContext(Dispatchers.IO) {
            val leaderId = getTrainee(traineeId)?.leaderId
            val categoryList = arrayListOf<Category>()
            if (leaderId != null) {
                val allTraineeCategoryJoinList =
                    getAllTraineeCategoryJoin().filter { it.idTrainee == traineeId }
                allTraineeCategoryJoinList.forEach { traineeCategoryJoin ->
                    getCategory(traineeCategoryJoin.idCategory)?.let { categoryList.add(it) }
                }
                return@withContext categoryList
            } else
                emptyList()
        }

    override suspend fun getCategory(categoryId: Int): Category? = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Category::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        if (dataSnapshot?.key == Category::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val category = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Category::class.java)
                }.filter { it.id == categoryId }
                if (category.isNotEmpty()) {
                    category[0]
                } else {
                    null
                }
            }
        } else {
            null
        }
    }

    override suspend fun deleteAllTraineeCategoryJoinForTrainee(traineeId: String): Long =
        withContext(Dispatchers.IO) {
            val getAllCategoryFromTraineeFilter =
                getAllTraineeCategoryJoin().filter { it.idTrainee == traineeId }
            getAllCategoryFromTraineeFilter.forEach {
                val result = deleteTraineeCategoryJoin(it.id)
                if (result == StatusCode.ERROR.value) {
                    return@withContext StatusCode.ERROR.value
                }
            }
            StatusCode.SUCCESS.value
        }

    override suspend fun deleteAllTraineeCategoryJoinForCategory(idCategory: Int): Long =
        withContext(Dispatchers.IO) {
            getAllTraineeCategoryJoin().filter { it.idCategory == idCategory }.forEach {
                val result = deleteTraineeCategoryJoin(it.id)
                if (result == StatusCode.ERROR.value) {
                    return@withContext StatusCode.ERROR.value
                }
            }
            deleteCategory(idCategory)
        }

    override suspend fun deleteTraineeCategoryJoin(traineeCategoryJoinId: Int): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(TraineeCategoryJoin::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(traineeCategoryJoinId.toString())
                ?.removeValue()
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            delay(500)
            resultCode
        }

    override suspend fun getAllTraineeCategoryJoin(): List<TraineeCategoryJoin> =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(TraineeCategoryJoin::class.simpleName.toString())
            val dataSnapshot = reference?.get()?.await()
            var traineeCategoryJoinList = mutableListOf<TraineeCategoryJoin>()
            if (dataSnapshot?.key == TraineeCategoryJoin::class.simpleName.toString()) {
                dataSnapshot.value?.let {
                    val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                    traineeCategoryJoinList.addAll(hashMapValues.values.map {
                        Gson().fromJson(Gson().toJson(it), TraineeCategoryJoin::class.java)
                    })
                }
            }
            traineeCategoryJoinList
        }

    override suspend fun insertLink(link: Link): Long = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Link::class.simpleName.toString())
        var resultCode: Long = StatusCode.ERROR.value
        reference?.child(link.id.toString())?.setValue(link)?.addOnCompleteListener { task ->
            resultCode = if (task.isSuccessful) {
                StatusCode.SUCCESS.value
            } else {
                StatusCode.ERROR.value
            }
        }?.await()
        resultCode
    }

    override suspend fun deleteLink(link: Link): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Link::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(link.id.toString())
                ?.removeValue()
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun getAllLink(leaderId: String): List<Link> =
        withContext(Dispatchers.IO) {
            getAllLink().filter { it.leaderMaterialId == leaderId }
        }

    override suspend fun getAllLink(): List<Link> = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Link::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        var links = mutableListOf<Link>()

        if (dataSnapshot?.key == Link::class.simpleName.toString()) {
            (dataSnapshot.value as? HashMap<*, *>)?.let { hashMapValues ->
                links.addAll(hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Link::class.java)
                })
            }
        }
        links
    }

    override suspend fun updateUrlLink(linkId: Int, link: String): Long =
        withContext(Dispatchers.IO) {
            val reference = FirebaseDataBase.database?.child(Link::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(linkId.toString())?.child(Link::url.name)
                ?.setValue(link)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun updateTitleLink(linkId: Int, newTitle: String): Long =
        withContext(Dispatchers.IO) {
            val reference = FirebaseDataBase.database?.child(Link::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(linkId.toString())?.child(Link::title.name)
                ?.setValue(newTitle)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun getLinksByCategory(leaderId: String, categoryId: Int): List<Link> =
        withContext(Dispatchers.IO) {
            val reference = FirebaseDataBase.database?.child(Link::class.simpleName.toString())
            val dataSnapshot = reference?.get()?.await()
            var links = mutableListOf<Link>()
            if (dataSnapshot?.key == Link::class.simpleName.toString()) {
                dataSnapshot.value?.let {
                    val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                    links.addAll(hashMapValues.values.map {
                        Gson().fromJson(Gson().toJson(it), Link::class.java)
                    }.filter { it.leaderMaterialId == leaderId && it.categoryId == categoryId })
                }
            }
            links
        }

    override suspend fun insertLeader(leader: Leader): Long = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
        var resultCode: Long = StatusCode.ERROR.value
        reference?.child(leader.userId)?.setValue(leader)?.addOnCompleteListener { task ->
            resultCode = if (task.isSuccessful) {
                StatusCode.SUCCESS.value
            } else {
                StatusCode.ERROR.value
            }
        }?.await()
        resultCode
    }

    override suspend fun insertTrainee(trainee: Trainee): Long = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
        var resultCode: Long = StatusCode.ERROR.value
        reference?.child(trainee.userId)?.setValue(trainee)?.addOnCompleteListener { task ->
            resultCode = if (task.isSuccessful) {
                StatusCode.SUCCESS.value
            } else {
                StatusCode.ERROR.value
            }
        }?.await()
        resultCode
    }

    override suspend fun updateTraineeName(idTrainee: String, name: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(idTrainee.toString())?.child(Trainee::name.name)
                ?.setValue(name)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun updateTraineeLastName(idTrainee: String, lastName: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(idTrainee.toString())?.child(Trainee::lastName.name)
                ?.setValue(lastName)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    // TODO: remove this method, update pass for user not por usertype
    override suspend fun updateTraineePassword(idTrainee: String, pass: String): Long =
        withContext(Dispatchers.IO) {
            var result: StatusCode = StatusCode.ERROR
            FirebaseAuth.getInstance().currentUser?.updatePassword(pass)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result = StatusCode.SUCCESS
                    } else {
                        result = StatusCode.ERROR
                    }
                }?.await()
            return@withContext result.value
        }

    override suspend fun updateUserPassword(pass: String): Long =
        withContext(Dispatchers.IO) {
            var result: StatusCode = StatusCode.ERROR
            FirebaseAuth.getInstance().currentUser?.updatePassword(pass)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result = StatusCode.SUCCESS
                    } else {
                        result = StatusCode.ERROR
                    }
                }?.await()
            return@withContext result.value
        }

    override suspend fun deleteLeader(leader: Leader): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(leader.userId.toString())
                ?.removeValue()
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun deleteTrainee(trainee: Trainee): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(trainee.userId.toString())
                ?.removeValue()
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun deleteAllLeader(): Long =
        withContext(Dispatchers.IO) {
            val leaderList = getAllLeader()
            val reference =
                FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            for (leader in leaderList) {
                reference?.child(leader.userId.toString())
                    ?.removeValue()
                    ?.addOnCompleteListener { task ->
                        resultCode = if (task.isSuccessful) {
                            StatusCode.SUCCESS.value
                        } else {
                            StatusCode.ERROR.value
                            return@addOnCompleteListener
                        }
                    }?.await()
            }
            resultCode
        }

    override suspend fun deleteAllTrainee(): Long =
        withContext(Dispatchers.IO) {
            val traineeList = getAllTrainee()
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            for (trainee in traineeList) {
                reference?.child(trainee.userId.toString())
                    ?.removeValue()
                    ?.addOnCompleteListener { task ->
                        resultCode = if (task.isSuccessful) {
                            StatusCode.SUCCESS.value
                        } else {
                            StatusCode.ERROR.value
                            return@addOnCompleteListener
                        }
                    }?.await()
            }
            resultCode
        }

    override suspend fun getUserType(userId: String): Flow<UserType?> = flow {
        val leader = getLeader(userId)
        if (leader != null) {
            emit(leader.userType)
            return@flow
        }
        val trainee = getTrainee(userId)
        if (trainee != null) {
            emit(trainee.userType)
            return@flow
        }
        emit(null)
        return@flow
    }

    override suspend fun getLeader(leaderId: String): Leader? = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        if (dataSnapshot?.key == Leader::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val leaders = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Leader::class.java)
                }.filter { it.userId == leaderId }
                if (leaders.isNotEmpty()) {
                    leaders[0]
                } else {
                    null
                }
            }
        } else {
            null
        }
    }

    override suspend fun getLeaderByEmail(leaderEmail: String): Leader? {
        val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()

        if (dataSnapshot?.key == Leader::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val leaders = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Leader::class.java)
                }.filter { it.email == leaderEmail }
                if (leaders.isNotEmpty()) {
                    return leaders[0]
                } else {
                    null
                }
            }
        } else {
            null
        }
        return null
    }

    override suspend fun getTrainee(traineeId: String): Trainee? = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        if (dataSnapshot?.key == Trainee::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val trainees = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Trainee::class.java)
                }.filter { it.userId == traineeId }
                if (trainees.isNotEmpty()) {
                    trainees[0]
                } else {
                    null
                }
            }
        } else {
            null
        }
    }

    override suspend fun getTraineeByEmail(traineeEmail: String): Trainee? {
        val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()

        if (dataSnapshot?.key == Trainee::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val trainees = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Trainee::class.java)
                }.filter { it.email == traineeEmail }
                if (trainees.isNotEmpty()) {
                    return trainees[0]
                } else {
                    null
                }
            }
        } else {
            null
        }
        return null
    }

    override suspend fun getAllTrainee(): List<Trainee> {
        val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        val traineeList = mutableListOf<Trainee>()

        if (dataSnapshot?.key == Trainee::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                traineeList.addAll(hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Trainee::class.java)
                })
            }
        }
        return traineeList
    }


    override suspend fun getAllLeader(): List<Leader> {
        val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        val leaderList = mutableListOf<Leader>()

        if (dataSnapshot?.key == Leader::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                leaderList.addAll(hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Leader::class.java)
                })
            }
        }
        return leaderList
    }

    override suspend fun setLinkedTrainee(traineeId: String, leaderId: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(traineeId.toString())?.child(Trainee::leaderId.name)
                ?.setValue(leaderId)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun getUnlinkedTrainees(): List<Trainee> = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        var unLinkedTraineeList = mutableListOf<Trainee>()
        if (dataSnapshot?.key == Trainee::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                unLinkedTraineeList.addAll(hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Trainee::class.java)
                }.filter { it.leaderId == null })
            }
        }
        unLinkedTraineeList
    }

    override suspend fun getLinkedTrainees(leaderId: String): List<Trainee> =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            val dataSnapshot = reference?.get()?.await()
            var unLinkedTraineeList = mutableListOf<Trainee>()
            if (dataSnapshot?.key == Trainee::class.simpleName.toString()) {
                dataSnapshot.value?.let {
                    val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                    unLinkedTraineeList.addAll(hashMapValues.values.map {
                        Gson().fromJson(Gson().toJson(it), Trainee::class.java)
                    }.filter { it.leaderId == leaderId })
                }
            }
            unLinkedTraineeList
        }

    override suspend fun setUnlinkedTrainee(traineeId: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(traineeId.toString())?.child(Trainee::leaderId.name)
                ?.setValue(null)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun updateTraineePosition(
        traineeId: String,
        traineePosition: Position
    ): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(traineeId.toString())?.child(Trainee::position.name)
                ?.setValue(traineePosition)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun updateLeaderName(leaderId: String, name: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(leaderId.toString())?.child(Leader::name.name)
                ?.setValue(name)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun updateLeaderLastName(leaderId: String, lastName: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(leaderId)?.child(Leader::lastName.name)
                ?.setValue(lastName)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

// Access Key Flow

    override suspend fun validateLeaderAccessKey(leaderId: String, accessKey: Int): Long {
        // TODO: "Not yet implemented"
        return StatusCode.SUCCESS.value
    }

    override suspend fun validateTraineeAccessKey(traineeId: String, accessKey: Int): Long {
        // TODO: "Not yet implemented"
        return StatusCode.SUCCESS.value
    }

    override suspend fun updateLeaderAccessKey(leaderId: String, accessKey: Int): Long {
        // TODO: "Not yet implemented"
        return StatusCode.SUCCESS.value
    }

    override suspend fun updateTraineeAccessKey(traineeIds: String, accessKey: Int): Long {
        // TODO: "Not yet implemented"
        return StatusCode.SUCCESS.value
    }

    override suspend fun signInWithToken(userId: String, token: String): Flow<StatusCode> =
        flow {
            try {
                if (FirebaseAuth.getInstance().signInWithEmailAndPassword(userId, token)
                        .addOnCompleteListener {}.await().user != null
                ) {
                    updateUserToken().collect {
                        if (it == StatusCode.SUCCESS) {
                            emit(StatusCode.SUCCESS)
                        } else {
                            emit(StatusCode.ERROR)
                        }
                    }
                } else
                    emit(StatusCode.ERROR)

            } catch (exception: Exception) {
                emit(StatusCode.ERROR)
            }

        }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        pass: String
    ): Flow<StatusCode> =
        flow {
            try {
                if (FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener {}.await().user != null
                ) {
                    updateUserToken().collect {
                        if (it == StatusCode.SUCCESS) {
                            emit(StatusCode.SUCCESS)
                        } else {
                            emit(StatusCode.ERROR)
                        }
                    }
                } else
                    emit(StatusCode.ERROR)
            } catch (exception: Exception) {
                emit(StatusCode.ERROR)
            }
        }

    override suspend fun updateUserToken(): Flow<StatusCode> {
        val result = CompletableFuture<StatusCode>()
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    result.complete(StatusCode.ERROR)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token =
                    Token(FirebaseAuth.getInstance().currentUser?.uid.toString(), task.result)
                FirebaseDataBase.database?.child(Token::class.simpleName.toString())
                    ?.child(token.userId)
                    ?.setValue(token)
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            result.complete(StatusCode.SUCCESS)
                        } else {
                            result.complete(StatusCode.ERROR)
                        }
                    }
            })
        } catch (exception: Exception) {
            result.complete(StatusCode.ERROR)
        }
        return flowOf(result.await())
    }

    override suspend fun getUserToken(): Flow<String> = flow {
        val result = CompletableFuture<String>()
        try {
            FirebaseDataBase.database?.child(Token::class.simpleName.toString())
                ?.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                ?.child(Token::token.name)
                ?.get()
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        result.complete(it.result.value.toString())
                    } else {
                        result.complete("")
                    }
                }
        } catch (exception: Exception) {
            result.complete("")
        }
        emit(result.await())
    }

    override suspend fun notify(
        fromToken: String,
        toToken: String,
        title: String,
        message: String
    ): Long {
        return try {
            val result = CompletableFuture<Long>()
            Log.e("TRAILEAD_NOTIF", "Sending FCM notification")
            fcmAPI.send(SendMessageDto(toToken, NotificationBody(title, message)))
                .enqueue(object : Callback<FcmResponse?> {
                    override fun onResponse(
                        call: Call<FcmResponse?>,
                        response: Response<FcmResponse?>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("TRAILEAD_NOTIF", "FCM notification sent")
                            result.complete(StatusCode.SUCCESS.value)
                        } else {
                            Log.e("TRAILEAD_NOTIF", "Failed to send FCM notification")
                            result.complete(StatusCode.ERROR.value)
                        }
                    }

                    override fun onFailure(call: Call<FcmResponse?>, t: Throwable) {
                        Log.e("TRAILEAD_NOTIF", "Failed to send FCM notification")
                        result.complete(StatusCode.ERROR.value)
                    }
                })
            Log.e("TRAILEAD_NOTIF", "Finish FCM notification")
            return result.await()
        } catch (exception: Exception) {
            Log.e("TRAILEAD_NOTIF", exception.stackTraceToString())
            StatusCode.ERROR.value
        }
    }

    override suspend fun getTokenByUser(userId: String): Flow<String> = flow {
        val result = CompletableFuture<String>()
        try {
            FirebaseDataBase.database?.child(Token::class.simpleName.toString())
                ?.child(userId)
                ?.child(Token::token.name)
                ?.get()
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        result.complete(it.result.value.toString())
                    } else {
                        result.complete("")
                    }
                }
        } catch (exception: Exception) {
            result.complete("")
        }
        emit(result.await())
    }
}
