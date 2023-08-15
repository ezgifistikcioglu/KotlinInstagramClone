package com.ezgieren.kotlininstagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ezgieren.kotlininstagramclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
    }
    fun signInClicked(view: View?) {
    }

    fun signUpClicked(view: View?) {
       val email = binding.idETEmailAddress.text.toString()
       val password = binding.idETPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                // Success
                val intent = Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                // Failure
                Toast.makeText(this@MainActivity,it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this,"Enter email and password!!", Toast.LENGTH_LONG).show()
        }
    }
}