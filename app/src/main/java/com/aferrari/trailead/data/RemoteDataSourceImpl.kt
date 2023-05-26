package com.aferrari.trailead.data

import android.util.Log
import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.StatusCode
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
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


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
        return suspendCoroutine { continuation ->
            FirebaseDataBase.database?.child(Leader::class.simpleName.toString())
                ?.child(leader.id.toString())
                ?.setValue(leader)?.addOnCompleteListener { task ->
                    val resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                    continuation.resume(resultCode)
                }
        }
    }

    override suspend fun insertTrainee(trainee: Trainee): Long {
        return suspendCoroutine { continuation ->
            FirebaseDataBase.database?.child(Trainee::class.simpleName.toString())
                ?.child(trainee.id.toString())
                ?.setValue(trainee)?.addOnCompleteListener { task ->
                    val resultCode = if (task.isSuccessful) {
                        StatusCode.SUCCESS.value
                    } else {
                        StatusCode.ERROR.value
                    }
                    continuation.resume(resultCode)
                }
        }
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
        return suspendCoroutine { continuation ->
            FirebaseDataBase.database?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val traineeList = mutableListOf<Trainee>()
                    if (dataSnapshot.exists()) {
                        // Recorre todos los hijos (objetos) de la referencia
                        for (childSnapshot in dataSnapshot.children) {
                            // Obtén los datos del objeto actual
                            if (childSnapshot.key == Trainee::class.java.simpleName) {
                                val traineeHashMap =
                                    (childSnapshot.value as HashMap<String, Object>).values

                                // Convierte el HashMap en un objeto User utilizando Gson
                                traineeHashMap.forEach {
                                    val gson = Gson()
                                    val traineeJson = gson.toJson(it)
                                    val trainee: Trainee =
                                        gson.fromJson(traineeJson, Trainee::class.java)
                                    traineeList.add(trainee)
                                }
                            }
                        }
                    }
                    continuation.resume(traineeList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // El método onCancelled se llama si ocurre algún error en la consulta
                    Log.e("Firebase", "Error al obtener los datos: " + databaseError.message)
                    continuation.resume(emptyList())
                }

            })
        }
    }

    private suspend fun uploadLeader(
        childSnapshot: DataSnapshot,
        localDataSource: LocalDataSource
    ) {
        if (childSnapshot.key == Leader::class.java.simpleName) {
            val leaderHashMap =
                (childSnapshot.value as HashMap<String, Object>).values

            // Convierte el HashMap en un objeto User utilizando Gson
            leaderHashMap.forEach {
                val gson = Gson()
                val leaderJson = gson.toJson(it)
                val leader: Leader =
                    gson.fromJson(leaderJson, Leader::class.java)
                if (localDataSource.getLeader(leader.email) != null) {
                    localDataSource.insertLeader(leader)
                }
            }
        }
    }

    private suspend fun uploadTrainee(
        childSnapshot: DataSnapshot,
        localDataSource: LocalDataSource
    ) {
        if (childSnapshot.key == Trainee::class.java.simpleName) {
            val leaderHashMap =
                (childSnapshot.value as HashMap<String, Object>).values

            // Convierte el HashMap en un objeto User utilizando Gson
            leaderHashMap.forEach {
                val gson = Gson()
                val traineeJson = gson.toJson(it)
                val trainee: Trainee =
                    gson.fromJson(traineeJson, Trainee::class.java)
                if (localDataSource.getLeader(trainee.email) != null) {
                    localDataSource.insertTrainee(trainee)
                }
            }
        }
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
        return suspendCoroutine { continuation ->
            FirebaseDataBase.database?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val leaderList = mutableListOf<Leader>()
                    if (dataSnapshot.exists()) {
                        // Recorre todos los hijos (objetos) de la referencia
                        for (childSnapshot in dataSnapshot.children) {
                            // Obtén los datos del objeto actual
                            if (childSnapshot.key == Leader::class.java.simpleName) {
                                val leaderHashMap =
                                    (childSnapshot.value as HashMap<String, Object>).values

                                // Convierte el HashMap en un objeto User utilizando Gson
                                leaderHashMap.forEach {
                                    val gson = Gson()
                                    val leaderJson = gson.toJson(it)
                                    val leader: Leader =
                                        gson.fromJson(leaderJson, Leader::class.java)
                                    leaderList.add(leader)
                                }
                            }
                        }
                    }
                    continuation.resume(leaderList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // El método onCancelled se llama si ocurre algún error en la consulta
                    Log.e("Firebase", "Error al obtener los datos: " + databaseError.message)
                    continuation.resume(emptyList())
                }

            })
        }
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