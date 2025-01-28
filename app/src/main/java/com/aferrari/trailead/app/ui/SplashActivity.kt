package com.aferrari.trailead.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.login.LoginViewModelFactory
import com.aferrari.trailead.app.viewmodel.login.SplashViewModel
import com.aferrari.trailead.common.StringUtils.DEEPLINK_LOGIN
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep
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
        initViewModel()

        lifecycleScope.launch {
            sleep(3000)
            withContext(Dispatchers.Main) {
                goLogin()
            }
        }
    }

    private fun initViewModel() {
        val factory = LoginViewModelFactory(remoteDataSource)
        splashViewModel = ViewModelProvider(this, factory)[SplashViewModel::class.java]
    }

    private fun goLogin() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(DEEPLINK_LOGIN)))
        finish()
    }

}
