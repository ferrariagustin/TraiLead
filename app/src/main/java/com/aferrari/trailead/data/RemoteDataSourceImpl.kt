package com.aferrari.trailead.data

import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.common_enum.UserType
import com.aferrari.trailead.data.db.FirebaseDataBase
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo
import com.google.gson.Gson
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RemoteDataSourceImpl @Inject constructor() : RemoteDataSource {
    override suspend fun insertYouTubeVideo(newYouTubeVideo: YouTubeVideo): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(YouTubeVideo::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(newYouTubeVideo.id.toString())?.setValue(newYouTubeVideo)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun getAllYoutubeVideo(leaderId: Int): List<YouTubeVideo> =
        withContext(Dispatchers.IO) {
            getAllYoutubeVideo().filter { it.leaderMaterialId == leaderId }
        }

    override suspend fun getAllYoutubeVideo(): List<YouTubeVideo> =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(YouTubeVideo::class.simpleName.toString())
            val dataSnapshot = reference?.get()?.await()
            var youtubeVideosList = mutableListOf<YouTubeVideo>()
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
        leaderId: Int,
        categoryId: Int
    ): List<YouTubeVideo> = withContext(Dispatchers.IO) {
        getAllYoutubeVideo(leaderId).filter { it.categoryId == categoryId }
    }

    override suspend fun insertCategory(category: Category): Long = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Category::class.simpleName.toString())
        var resultCode: Long = StatusCode.ERROR.value
        reference?.child(category.id.toString())?.setValue(category)
            ?.addOnCompleteListener { task ->
                resultCode = if (task.isSuccessful) {
                    StatusCode.SUCCESS.value
                } else {
                    StatusCode.ERROR.value
                }
            }?.await()
        resultCode
    }

    override suspend fun getAllCategory(leaderId: Int): List<Category> =
        withContext(Dispatchers.IO) {
            getAllCategory().filter { it.leaderCategoryId == leaderId }
        }

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

    override suspend fun deleteCategory(category: Category): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Category::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(category.id.toString())
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

    override suspend fun updateCategory(categoryId: Int, categoryName: String): Long =
        withContext(Dispatchers.IO) {
            val reference = FirebaseDataBase.database?.child(Category::class.simpleName.toString())
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
            StatusCode.SUCCESS.value
        }

    override suspend fun insertCategoryFromTrainee(traineeCategoryJoin: TraineeCategoryJoin): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(TraineeCategoryJoin::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(traineeCategoryJoin.id.toString())?.setValue(traineeCategoryJoin)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun getCategoriesFromTrainee(traineeId: Int): List<Category> =
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

    override suspend fun deleteAllTraineeCategoryJoinForTrainee(traineeId: Int): Long =
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

    override suspend fun deleteAllTraineeCategoryJoinForCategory(categoryId: Int): Long =
        withContext(Dispatchers.IO) {
            val getAllCategoryFromTraineeFilter =
                getAllTraineeCategoryJoin().filter { it.idCategory == categoryId }
            getAllCategoryFromTraineeFilter.forEach {
                val result = deleteTraineeCategoryJoin(it.id)
                if (result == StatusCode.ERROR.value) {
                    return@withContext StatusCode.ERROR.value
                }
            }
            StatusCode.SUCCESS.value
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

    override suspend fun getAllLink(leaderId: Int): List<Link> = withContext(Dispatchers.IO) {
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

    override suspend fun getLinkByCategory(leaderId: Int, categoryId: Int): List<Link> =
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
        reference?.child(leader.id.toString())?.setValue(leader)?.addOnCompleteListener { task ->
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
        reference?.child(trainee.id.toString())?.setValue(trainee)?.addOnCompleteListener { task ->
            resultCode = if (task.isSuccessful) {
                StatusCode.SUCCESS.value
            } else {
                StatusCode.ERROR.value
            }
        }?.await()
        resultCode
    }

    override suspend fun updateTraineeName(idTrainee: Int, name: String): Long =
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

    override suspend fun updateTraineeLastName(idTrainee: Int, lastName: String): Long =
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

    override suspend fun updateTraineePassword(idTrainee: Int, pass: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(idTrainee.toString())?.child(Trainee::pass.name)
                ?.setValue(pass)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun deleteLeader(leader: Leader): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(leader.id.toString())
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
            reference?.child(trainee.id.toString())
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
            val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            for (leader in leaderList) {
                reference?.child(leader.id.toString())
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
            val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            for (trainee in traineeList) {
                reference?.child(trainee.id.toString())
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

    override suspend fun getUserType(user_id: Int): Flow<UserType?> = flow {
        val leader = getLeader(user_id)
        if (leader != null) {
            emit(leader.userType)
            return@flow
        }
        val trainee = getTrainee(user_id)
        if (trainee != null) {
            emit(trainee.userType)
            return@flow
        }
        emit(null)
        return@flow
    }

    override suspend fun getLeader(leader_id: Int): Leader? = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        if (dataSnapshot?.key == Leader::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val leaders = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Leader::class.java)
                }.filter { it.id == leader_id }
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

    override suspend fun getLeader(user_email: String, user_pass: String): Leader? {
        val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()

        if (dataSnapshot?.key == Leader::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val leaders = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Leader::class.java)
                }.filter { it.email == user_email && it.pass == user_pass }
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

    override suspend fun getLeader(leader_email: String): Leader? {
        val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()

        if (dataSnapshot?.key == Leader::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val leaders = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Leader::class.java)
                }.filter { it.email == leader_email }
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

    override suspend fun getTrainee(trainee_id: Int): Trainee? = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        if (dataSnapshot?.key == Trainee::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val trainees = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Trainee::class.java)
                }.filter { it.id == trainee_id }
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

    override suspend fun getTrainee(user_email: String, user_pass: String): Trainee? {
        val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        if (dataSnapshot?.key == Trainee::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val trainees = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Trainee::class.java)
                }.filter { it.email == user_email && it.pass == user_pass }
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

    override suspend fun getTrainee(trainee_email: String): Trainee? {
        val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()

        if (dataSnapshot?.key == Trainee::class.simpleName.toString()) {
            dataSnapshot.value?.let {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                val trainees = hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Trainee::class.java)
                }.filter { it.email == trainee_email }
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

    override suspend fun setLinkedTrainee(trainee_id: Int, leader_id: Int): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(trainee_id.toString())?.child(Trainee::leaderId.name)
                ?.setValue(leader_id)
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

    override suspend fun getLinkedTrainees(leader_id: Int): List<Trainee> =
        withContext(Dispatchers.IO) {
            val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            val dataSnapshot = reference?.get()?.await()
            var unLinkedTraineeList = mutableListOf<Trainee>()
            if (dataSnapshot?.key == Trainee::class.simpleName.toString()) {
                dataSnapshot.value?.let {
                    val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                    unLinkedTraineeList.addAll(hashMapValues.values.map {
                        Gson().fromJson(Gson().toJson(it), Trainee::class.java)
                    }.filter { it.leaderId == leader_id })
                }
            }
            unLinkedTraineeList
        }

    override suspend fun setUnlinkedTrainee(trainee_id: Int): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(trainee_id.toString())?.child(Trainee::leaderId.name)
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

    override suspend fun updateTraineePosition(trainee_id: Int, trainee_position: Position): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(trainee_id.toString())?.child(Trainee::position.name)
                ?.setValue(trainee_position)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }

    override suspend fun updateLeaderName(leaderId: Int, name: String): Long =
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

    override suspend fun updateLeaderLastName(leaderId: Int, lastName: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(leaderId.toString())?.child(Leader::lastName.name)
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

    override suspend fun updateLeaderPassword(leaderId: Int, pass: String): Long =
        withContext(Dispatchers.IO) {
            val reference =
                FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
            var resultCode: Long = StatusCode.ERROR.value
            reference?.child(leaderId.toString())?.child(Leader::pass.name)
                ?.setValue(pass)
                ?.addOnCompleteListener { task ->
                    resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                }?.await()
            resultCode
        }
}