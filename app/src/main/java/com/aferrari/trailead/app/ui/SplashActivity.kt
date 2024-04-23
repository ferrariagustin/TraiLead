package com.aferrari.trailead.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aferrari.trailead.app.viewmodel.login.LoginViewModelFactory
import com.aferrari.trailead.app.viewmodel.login.SplashViewModel
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = LoginViewModelFactory(remoteDataSource)
        splashViewModel = ViewModelProvider(this, factory)[SplashViewModel::class.java]
        val loginActivity = Intent(Intent.ACTION_VIEW, Uri.parse("trailead://login"))
        this.startActivity(loginActivity)
        finish()
    }

}