package com.aferrari.trailead.app.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LoginActivityBinding
import com.aferrari.trailead.notification.NotificationManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity)
        NotificationManager.createNotificationChannel(this)
    }
}