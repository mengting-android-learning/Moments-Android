package com.example.momentsrecyclerview.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.momentsrecyclerview.databinding.ListItemTwwetsBinding
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.util.setImageUrl

class TweetsAdapter :
    ListAdapter<Tweet, TweetsAdapter.TweetViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        return TweetViewHolder(
            ListItemTwwetsBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Tweet>() {
        override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet): Boolean {
            return oldItem == newItem
        }
    }

    class TweetViewHolder(private var binding: ListItemTwwetsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val commentAdapter = TweetCommentsAdapter()
        init {
            binding.commentsList.adapter = commentAdapter
        }
        fun bind(tweet: Tweet) {
            binding.tweet = tweet
            addImages(tweet)
            commentAdapter.submitList(tweet.comments)
            binding.executePendingBindings()
        }

        private fun addImages(tweet: Tweet) {
            val images: List<ImageUrl>? = tweet.images
            if (images?.isNotEmpty() == true) {
                binding.senderImages.removeAllViews()
                val columnCount = mapOf(1 to 1, 2 to 2, 4 to 2)
                binding.senderImages.columnCount =
                    if (columnCount.containsKey(images.size)) columnCount[images.size]!! else 3
                binding.senderImages.rowCount = ((images.size - 1) / 3) + 1
                for (i in images.indices) {
                    val imageViewNew = ImageView(binding.senderImages.context)
                    imageViewNew.scaleType = ImageView.ScaleType.CENTER_CROP
                    val padding = 5
                    imageViewNew.setPadding(padding, padding, padding, padding)
                    setImageUrl(imageViewNew, images[i].url)
                    binding.senderImages.addView(imageViewNew, 300, 300)
                }
            }
        }
    }
}
