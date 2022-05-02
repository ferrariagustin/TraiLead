package com.aferrari.trailead.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity(), Login {

    companion object {
        const val DEEPLINK_HOME = "trailead://home"
    }

    private lateinit var loginActivityBinding: LoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityBinding = DataBindingUtil.setContentView(this, R.layout.login_activity)

        loginActivityBinding.loginBtn.setOnClickListener {
            login()
        }
    }

    override fun login() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEEPLINK_HOME))
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}