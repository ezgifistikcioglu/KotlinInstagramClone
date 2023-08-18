package com.ezgieren.kotlininstagramclone

import android.content.Context
import android.widget.Toast

class CustomFunc(private val context: Context) {

    fun handleFailure(exception: Exception) {
        exception.localizedMessage?.let { errorMessage ->
            showToast(errorMessage)
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}