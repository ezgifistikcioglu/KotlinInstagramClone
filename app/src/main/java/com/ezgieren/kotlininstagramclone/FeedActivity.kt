package com.ezgieren.kotlininstagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezgieren.kotlininstagramclone.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}