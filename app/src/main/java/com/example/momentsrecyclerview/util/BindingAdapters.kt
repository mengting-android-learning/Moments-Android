package com.example.momentsrecyclerview.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.ui.TweetsAdapter
import com.example.momentsrecyclerview.viewmodels.STATUS

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    url?.let {
        Glide.with(imageView.context).load(it)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imageView)
    }
}

@BindingAdapter("userInfo", "tweetsList")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    userInfo: UserInfo?,
    tweets: List<Tweet>?
) {
    val adapter = recyclerView.adapter as TweetsAdapter
    adapter.allItems(userInfo, tweets)
}

@BindingAdapter("status")
fun bindStatus(
    statusImageView: ImageView,
    status: STATUS?
) {
    when (status!!) {
        STATUS.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        STATUS.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_broken_image)
        }
        STATUS.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}
