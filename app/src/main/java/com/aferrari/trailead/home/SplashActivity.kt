package com.aferrari.trailead.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginActivity = Intent(Intent.ACTION_VIEW, Uri.parse("trailead://login"))
        this.startActivity(loginActivity)
        finish()
    }

}