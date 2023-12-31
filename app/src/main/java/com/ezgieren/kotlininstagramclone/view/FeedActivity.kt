package com.ezgieren.kotlininstagramclone.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezgieren.kotlininstagramclone.CustomFunc
import com.ezgieren.kotlininstagramclone.R
import com.ezgieren.kotlininstagramclone.adapter.FeedRecyclerAdapter
import com.ezgieren.kotlininstagramclone.databinding.ActivityFeedBinding
import com.ezgieren.kotlininstagramclone.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    private lateinit var customFunc: CustomFunc

    private lateinit var postArrayList: ArrayList<Post>

    private lateinit var feedAdapter: FeedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customFunc = CustomFunc(this@FeedActivity)

        auth = Firebase.auth
        database = Firebase.firestore

        postArrayList = ArrayList()

        getData()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = feedAdapter
    }

    private fun getData() {
        database.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error != null) {
                error.localizedMessage?.let { customFunc.showToast(it) }
            } else {
                if (value != null) {
                    if (!value.isEmpty) {
                        val myDocuments = value.documents

                        postArrayList.clear()

                        for (document in myDocuments) {
                            val comment = document.get("comment") as String
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String


                            val post = Post(userEmail, comment, downloadUrl)
                            postArrayList.add(post)
                        }
                        feedAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.instagram_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_post) {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.signOut) {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}