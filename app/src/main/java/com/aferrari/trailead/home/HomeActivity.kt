package com.aferrari.trailead.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils.DEEPLINK_LOGIN
import com.aferrari.login.utils.StringUtils.USER_NAME_KEY
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.HomeActivityBinding

/**
 * Moved to other package or module
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var homeActivityBinding: HomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivityBinding = DataBindingUtil.setContentView(this, R.layout.home_activity)
        initComponent()
        initListeners()
    }

    private fun initListeners() {
        homeActivityBinding.logoutBtn.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        SessionManagement(this).removeSession()
        goLogin()
    }

    private fun goLogin() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEEPLINK_LOGIN))
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun initComponent() {
        homeActivityBinding.UserName.text = intent.extras?.get(USER_NAME_KEY) as String
    }
}