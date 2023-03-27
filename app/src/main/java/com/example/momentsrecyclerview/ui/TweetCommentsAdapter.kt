package com.example.momentsrecyclerview.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.momentsrecyclerview.data.TweetComment
import com.example.momentsrecyclerview.databinding.ListItemCommentsBinding

class TweetCommentsAdapter :
    ListAdapter<TweetComment, TweetCommentsAdapter.TweetCommentsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetCommentsViewHolder {
        return TweetCommentsViewHolder(
            ListItemCommentsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TweetCommentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TweetComment>() {

        override fun areItemsTheSame(oldItem: TweetComment, newItem: TweetComment): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TweetComment, newItem: TweetComment): Boolean {
            return oldItem == newItem
        }
    }

    class TweetCommentsViewHolder(private var binding: ListItemCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: TweetComment) {
            binding.comment = comment
            binding.executePendingBindings()
        }
    }
}
