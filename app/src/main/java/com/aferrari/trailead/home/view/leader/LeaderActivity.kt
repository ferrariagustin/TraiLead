package com.aferrari.trailead.home.view.leader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderActivityBinding

class LeaderActivity : AppCompatActivity() {

    private lateinit var binding: LeaderActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.leader_activity)
    }
}