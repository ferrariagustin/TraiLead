package com.aferrari.trailead.home.configurer

import com.aferrari.login.data.FirebaseDataBase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseConfigurer {

    private var database: DatabaseReference = Firebase.database.reference

    fun configure() {
        FirebaseDataBase.database = database
    }
}