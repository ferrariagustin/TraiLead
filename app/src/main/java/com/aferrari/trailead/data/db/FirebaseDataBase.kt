package com.aferrari.trailead.data.db

import com.aferrari.trailead.domain.models.Leader
import com.google.firebase.database.DatabaseReference

class FirebaseDataBase {

    companion object {
        var database: DatabaseReference? = null
    }

    fun insertLeader(leader: Leader) =
        database?.child(Leader::class.simpleName.toString())?.child(leader.id.toString())
            ?.setValue(leader)

    fun getLeader(email: String, pass: String) {
        database?.child(Leader::class.simpleName.toString())?.child(Leader::email.name)?.get()
    }

}