package com.aferrari.trailead.home.view.trainee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.TraineeActivityBinding

class TraineeActivity : AppCompatActivity() {

    private lateinit var binding: TraineeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.trainee_activity)
    }
}