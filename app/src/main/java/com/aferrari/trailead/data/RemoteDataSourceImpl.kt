package com.aferrari.trailead.data

import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.common_enum.UserType
import com.aferrari.trailead.data.db.FirebaseDataBase
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.LeaderWithTrainee
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.TraineeCategoryJoin
import com.aferrari.trailead.domain.models.YouTubeVideo
import com.google.firebase.database.DataSnapshot
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

    override suspend fun getAllYoutubeVideo(leaderId: Int): List<YouTubeVideo> = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(YouTubeVideo::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        var youtubeVideosList = mutableListOf<YouTubeVideo>()
        if (dataSnapshot?.key == YouTubeVideo::class.simpleName.toString()) {
            val hashMapValues = dataSnapshot.value as HashMap<String, Object>
            youtubeVideosList.addAll(hashMapValues.values.map {
                Gson().fromJson(Gson().toJson(it), YouTubeVideo::class.java)
            }.filter { it.leaderMaterialId == leaderId })
        }
        youtubeVideosList
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
            val reference = FirebaseDataBase.database?.child(Category::class.simpleName.toString())
            val dataSnapshot = reference?.get()?.await()
            var categories = mutableListOf<Category>()
            if (dataSnapshot?.key == Category::class.simpleName.toString()) {
                val hashMapValues = dataSnapshot.value as HashMap<String, Object>
                categories.addAll(hashMapValues.values.map {
                    Gson().fromJson(Gson().toJson(it), Category::class.java)
                }.filter { it.leaderCategoryId == leaderId })
            }
            categories
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

    override suspend fun deleteLink(link: Link) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLink(leaderId: Int): List<Link> = withContext(Dispatchers.IO) {
        val reference = FirebaseDataBase.database?.child(Link::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        var links = mutableListOf<Link>()
        if (dataSnapshot?.key == Link::class.simpleName.toString()) {
            val hashMapValues = dataSnapshot.value as HashMap<String, Object>
            links.addAll(hashMapValues.values.map {
                Gson().fromJson(Gson().toJson(it), Link::class.java)
            }.filter { it.leaderMaterialId == leaderId })
        }
        links
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

    override suspend fun updateLeader(leader: Leader): Long = withContext(Dispatchers.IO) {
        TODO("Buscar como actualizar un valor")
//        val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
//        var resultCode: Long = StatusCode.ERROR.value
//        reference?.child(leader.id.toString())?.updateChildren(leader)
//            ?.addOnCompleteListener { task ->
//                resultCode = if (task.isSuccessful) {
//                    StatusCode.SUCCESS.value
//                } else {
//                    StatusCode.ERROR.value
//                }
//            }?.await()
//        resultCode
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
            val hashMapValues = dataSnapshot.value as HashMap<String, Object>
            val leaders = hashMapValues.values.map {
                Gson().fromJson(Gson().toJson(it), Leader::class.java)
            }.filter { it.id == leader_id }
            if (leaders.isNotEmpty()) {
                leaders[0]
            } else {
                null
            }
        } else {
            null
        }
    }

    override suspend fun getLeader(user_email: String, user_pass: String): Leader? {
        val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()

        if (dataSnapshot?.key == Leader::class.simpleName.toString()) {
            val hashMapValues = dataSnapshot.value as HashMap<String, Object>
            val leaders = hashMapValues.values.map {
                Gson().fromJson(Gson().toJson(it), Leader::class.java)
            }.filter { it.email == user_email && it.pass == user_pass }
            if (leaders.isNotEmpty()) {
                return leaders[0]
            } else {
                null
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
            val hashMapValues = dataSnapshot.value as HashMap<String, Object>
            val leaders = hashMapValues.values.map {
                Gson().fromJson(Gson().toJson(it), Leader::class.java)
            }.filter { it.email == leader_email }
            if (leaders.isNotEmpty()) {
                return leaders[0]
            } else {
                null
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
            val hashMapValues = dataSnapshot.value as HashMap<String, Object>
            val trainees = hashMapValues.values.map {
                Gson().fromJson(Gson().toJson(it), Trainee::class.java)
            }.filter { it.id == trainee_id }
            if (trainees.isNotEmpty()) {
                trainees[0]
            } else {
                null
            }
        } else {
            null
        }
    }

    override suspend fun getTrainee(user_email: String, user_pass: String): Trainee? {
        val reference = FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()

        if (dataSnapshot?.key == Trainee::class.simpleName.toString()) {
            val hashMapValues = dataSnapshot.value as HashMap<String, Object>
            val trainees = hashMapValues.values.map {
                Gson().fromJson(Gson().toJson(it), Trainee::class.java)
            }.filter { it.email == user_email && it.pass == user_pass }
            if (trainees.isNotEmpty()) {
                return trainees[0]
            } else {
                null
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
            val hashMapValues = dataSnapshot.value as HashMap<String, Object>
            val trainees = hashMapValues.values.map {
                Gson().fromJson(Gson().toJson(it), Trainee::class.java)
            }.filter { it.email == trainee_email }
            if (trainees.isNotEmpty()) {
                return trainees[0]
            } else {
                null
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
            val hashMapValues = dataSnapshot.value as HashMap<String, Object>
            traineeList.addAll(hashMapValues.values.map {
                Gson().fromJson(Gson().toJson(it), Trainee::class.java)
            })
        }
        return traineeList
    }


    private suspend fun uploadLink(
        childSnapshot: DataSnapshot,
        localDataSource: LocalDataSource
    ) {
        if (childSnapshot.key == Link::class.java.simpleName) {
            val leaderHashMap =
                (childSnapshot.value as HashMap<String, Object>).values

            // Convierte el HashMap en un objeto User utilizando Gson
            leaderHashMap.forEach {
                val gson = Gson()
                val linkJson = gson.toJson(it)
                val link: Link =
                    gson.fromJson(linkJson, Link::class.java)
                if (localDataSource.getLeader(link.id) != null) {
                    localDataSource.insertLink(link)
                }
            }
        }
    }

    private suspend fun uploadYouTubeVideo(
        childSnapshot: DataSnapshot,
        localDataSource: LocalDataSource
    ) {
        if (childSnapshot.key == YouTubeVideo::class.java.simpleName) {
            val leaderHashMap =
                (childSnapshot.value as HashMap<String, Object>).values

            // Convierte el HashMap en un objeto User utilizando Gson
            leaderHashMap.forEach {
                val gson = Gson()
                val youtubeVideoJson = gson.toJson(it)
                val youTubeVideo: YouTubeVideo =
                    gson.fromJson(youtubeVideoJson, YouTubeVideo::class.java)
                suspend {
                    youTubeVideo.categoryId?.let { categoryId ->
                        if (!localDataSource.getYoutubeVideoByCategory(
                                youTubeVideo.leaderMaterialId,
                                categoryId
                            ).contains(youTubeVideo)
                        ) {
                            localDataSource.insertYouTubeVideo(youTubeVideo)
                        }
                    }
                }
            }
        }
    }

    // TODO: Resolved List
    private fun uploadAllCategoryFromTrainee(
        childSnapshot: DataSnapshot,
        localDataSource: LocalDataSource
    ) {
        if (childSnapshot.key == TraineeCategoryJoin::class.java.simpleName) {
            val leaderHashMap =
                (childSnapshot.value as HashMap<String, List<Object>>).values

            // Convierte el HashMap en un objeto User utilizando Gson
            leaderHashMap.forEach {
                val gson = Gson()
                val traineCategoryJoinJson = gson.toJson(it)
                val traineeCategoryJoin =
                    gson.fromJson(traineCategoryJoinJson, TraineeCategoryJoin::class.java)
                suspend {
//                    localDataSource.insertAllCategoryFromTrainee(traineeCategoryJoin)
                }
            }
        }
    }

    override suspend fun getAllLeader(): List<Leader> {
        val reference = FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
        val dataSnapshot = reference?.get()?.await()
        val leaderList = mutableListOf<Leader>()

        if (dataSnapshot?.key == Leader::class.simpleName.toString()) {
            val hashMapValues = dataSnapshot.value as HashMap<String, Object>
            leaderList.addAll(hashMapValues.values.map {
                Gson().fromJson(Gson().toJson(it), Leader::class.java)
            })
        }
        return leaderList
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