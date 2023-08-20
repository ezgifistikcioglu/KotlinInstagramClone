package com.ezgieren.kotlininstagramclone.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ezgieren.kotlininstagramclone.CustomFunc
import com.ezgieren.kotlininstagramclone.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    var selectedPicture: Uri? = null
    private lateinit var customFunc: CustomFunc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerLauncher()
        customFunc = CustomFunc(this@UploadActivity)
        auth = Firebase.auth
        fireStore = Firebase.firestore
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
                showPermissionSnackBar()
            } else {
                requestStoragePermission()
            }
        } else {
            openGallery()
        }
    }

    private fun showPermissionSnackBar() {
        Snackbar.make(binding.root, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
            .setAction("Give Permission") {
                requestStoragePermission()
            }.show()
    }

    private fun requestStoragePermission() {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun openGallery() {
        val intentToGallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intentToGallery)
    }

    fun uploadClicked(view: View) {
        checkAndRequestPermission()

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        storage = Firebase.storage
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)
        if (selectedPicture != null) {
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                //download url -> firestore
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()

                    val postMap = hashMapOf<String, Any>()
                    postMap["downloadUrl"] = downloadUrl
                    postMap["userEmail"] = auth.currentUser!!.email!!
                    postMap["comment"] = binding.idETCommentText.text.toString()
                    postMap["date"] = Timestamp.now()

                    fireStore.collection("Posts").add(postMap).addOnSuccessListener {
                        finish()
                    }

                }.addOnFailureListener { exception ->
                    customFunc.handleFailure(exception)
                }
            }.addOnFailureListener { exception ->
                customFunc.handleFailure(exception)
            }
        }
    }

    fun selectImageClicked(view: View) {
        checkAndRequestPermission()
    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedPicture = intentFromResult.data
                        selectedPicture?.let { uri ->
                            binding.idImageView.setImageURI(uri)
                        }
                    }
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    // Permission granted
                    openGallery()
                } else {
                    // Permission denied
                    customFunc.showToast("Permission needed!")
                }
            }
    }
}