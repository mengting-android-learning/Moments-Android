package com.example.momentsrecyclerview.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.momentsrecyclerview.databinding.ListItemTwwetsBinding
import com.example.momentsrecyclerview.domain.Tweet

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
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet): Boolean {
            TODO("Not yet implemented")
        }
    }

    class TweetViewHolder(private var binding: ListItemTwwetsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tweet: Tweet) {
            binding.tweet = tweet
            binding.executePendingBindings()
        }
    }
}
