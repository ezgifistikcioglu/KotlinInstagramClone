package com.ezgieren.kotlininstagramclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezgieren.kotlininstagramclone.databinding.RecyclerRowBinding
import com.ezgieren.kotlininstagramclone.model.Post
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(val postList: ArrayList<Post>) :
    RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {
    class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.idETEmailAddressText.text = postList[position].email
        holder.binding.captionTextView.text = postList[position].comment
        val url = postList[position].downloadUrl
        Picasso.get().load(url).into(holder.binding.postImageView);

    }

    override fun getItemCount(): Int {
        return postList.size
    }
}