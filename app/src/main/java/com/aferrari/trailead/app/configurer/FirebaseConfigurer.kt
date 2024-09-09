package com.aferrari.trailead.app.configurer

import com.aferrari.trailead.data.db.FirebaseDataBase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class FirebaseConfigurer {

    private var database: DatabaseReference = Firebase.database.reference

    fun configure() {
        FirebaseDataBase.database = database
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("Fetching FCM registration token failed")
                return@addOnCompleteListener
            }
            val token = task.result
            println("FCM registration token: $token")
        }
    }
}