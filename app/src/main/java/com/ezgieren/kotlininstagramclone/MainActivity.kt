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
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if (currentUser != null) {
            goToFeedActivityIntent()
        }
    }

    private fun goToFeedActivityIntent() {
        val intent = Intent(this@MainActivity, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun authenticateAndNavigate(
        view: View?,
        authAction: (email: String, password: String) -> Unit,
    ) {
        val email = binding.idETEmailAddress.text.toString()
        val password = binding.idETPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            authAction(email, password)
        } else {
            showToast("Enter email and password!!")        }
    }

    fun signInClicked(view: View?) {
        authenticateAndNavigate(view) { email, password ->
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                goToFeedActivityIntent()
            }.addOnFailureListener {
                it.localizedMessage?.let { exception -> showToast(exception) }
            }
        }
    }

    fun signUpClicked(view: View?) {
        authenticateAndNavigate(view) { email, password ->
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                goToFeedActivityIntent()
            }.addOnFailureListener {
                it.localizedMessage?.let { exception -> showToast(exception) }
            }
        }
    }
}