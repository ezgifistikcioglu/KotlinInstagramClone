package com.ezgieren.kotlininstagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ezgieren.kotlininstagramclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun signInClicked(view: View?) {
    }

    fun signUpClicked(view: View?) {
    }
}