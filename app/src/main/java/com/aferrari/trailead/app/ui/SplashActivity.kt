package com.aferrari.trailead.app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aferrari.trailead.app.configurer.FirebaseConfigurer

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseConfigurer().configure()
        val loginActivity = Intent(Intent.ACTION_VIEW, Uri.parse("trailead://login"))
        this.startActivity(loginActivity)
        finish()
    }

}