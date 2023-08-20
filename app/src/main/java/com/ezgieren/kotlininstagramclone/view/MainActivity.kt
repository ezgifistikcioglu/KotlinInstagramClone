package com.ezgieren.kotlininstagramclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ezgieren.kotlininstagramclone.CustomFunc
import com.ezgieren.kotlininstagramclone.R
import com.ezgieren.kotlininstagramclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    private lateinit var customFunc: CustomFunc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customFunc = CustomFunc(this@MainActivity)

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

    private fun authenticateAndNavigate(
        view: View?,
        authAction: (email: String, password: String) -> Unit,
    ) {
        val email = binding.idETEmailAddressText.text.toString()
        val password = binding.idETPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            authAction(email, password)
        } else {
            customFunc.showToast(getString(R.string.validEmailOrPw))
        }
    }

    fun signInClicked(view: View?) {
        authenticateAndNavigate(view) { email, password ->
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                goToFeedActivityIntent()
            }.addOnFailureListener {
                it.localizedMessage?.let { exception -> customFunc.showToast(exception) }
            }
        }
    }

    fun signUpClicked(view: View?) {
        authenticateAndNavigate(view) { email, password ->
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                goToFeedActivityIntent()
            }.addOnFailureListener {
                it.localizedMessage?.let { exception -> customFunc.showToast(exception) }
            }
        }
    }
}