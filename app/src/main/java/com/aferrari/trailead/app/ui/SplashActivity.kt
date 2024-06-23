package com.aferrari.trailead.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aferrari.trailead.R
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
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.motion_layout)

        Handler().postDelayed({
            val factory = LoginViewModelFactory(remoteDataSource)
            splashViewModel = ViewModelProvider(this, factory)[SplashViewModel::class.java]
            val loginActivity = Intent(Intent.ACTION_VIEW, Uri.parse("trailead://login"))
            this.startActivity(loginActivity)
            finish()
        }, 2000)
    }

}