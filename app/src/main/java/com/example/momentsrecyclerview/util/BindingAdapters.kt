package com.example.momentsrecyclerview.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.ui.STATUS
import com.example.momentsrecyclerview.ui.view.TweetsAdapter

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    url?.let {
        Glide.with(imageView.context).load(it).placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image).into(imageView)
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

@BindingAdapter("status", "tweetsList")
fun bindStatus(
    statusImageView: ImageView,
    status: STATUS?,
    tweets: List<Tweet>?
) {
    when (status!!) {
        STATUS.LOADING ->
            if (tweets == null) {
                statusImageView.setImageResource(R.drawable.loading_animation)
            } else {
                statusImageView.visibility = View.GONE
            }

        STATUS.ERROR ->
            if (tweets == null) {
                statusImageView.setImageResource(R.drawable.ic_connection_error)
            } else {
                statusImageView.visibility = View.GONE
            }

        STATUS.DONE -> statusImageView.visibility = View.GONE
    }
}
