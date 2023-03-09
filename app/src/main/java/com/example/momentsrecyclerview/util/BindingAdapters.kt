package com.example.momentsrecyclerview.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.ui.TweetsAdapter

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    url?.let {
        Glide.with(imageView.context).load(it).into(imageView)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<Tweet>?
) {
    val adapter = recyclerView.adapter as TweetsAdapter
    adapter.submitList(data)
}
