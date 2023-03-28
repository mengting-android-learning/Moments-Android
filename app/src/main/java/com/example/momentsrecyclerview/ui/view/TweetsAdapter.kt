package com.example.momentsrecyclerview.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.momentsrecyclerview.databinding.ListItemTweetsBinding
import com.example.momentsrecyclerview.databinding.UserInfoBinding
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.util.setImageUrl

private const val ITEM_VIEW_TYPE_USER_INFO = 0
private const val ITEM_VIEW_TYPE_TWEET = 1

class TweetsAdapter : ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_USER_INFO -> UserInfoViewHolder(
                UserInfoBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ),
                    parent,
                    false
                )
            )
            ITEM_VIEW_TYPE_TWEET -> TweetViewHolder(
                ListItemTweetsBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ),
                    parent,
                    false
                )
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserInfoViewHolder -> {
                val userInfoItem = getItem(position) as DataItem.UserInfoItem
                holder.bind(userInfoItem.userInfo)
            }
            is TweetViewHolder -> {
                val tweetItem = getItem(position) as DataItem.TweetItem
                holder.bind(tweetItem.tweet)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.UserInfoItem -> ITEM_VIEW_TYPE_USER_INFO
            is DataItem.TweetItem -> ITEM_VIEW_TYPE_TWEET
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    class TweetViewHolder(private var binding: ListItemTweetsBinding) :
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

    class UserInfoViewHolder(private var binding: UserInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userInfo: UserInfo) {
            binding.userInfo = userInfo
            binding.executePendingBindings()
        }
    }

    fun allItems(userInfo: UserInfo?, tweets: List<Tweet>?) {
        val items = when (userInfo) {
            null -> tweets?.map { DataItem.TweetItem(it) }
            else -> if (tweets != null) {
                listOf(DataItem.UserInfoItem(userInfo)) + tweets.map { DataItem.TweetItem(it) }
            } else {
                listOf(
                    DataItem.UserInfoItem(userInfo)
                )
            }
        }
        submitList(items)
    }
}

sealed class DataItem {

    abstract val id: Int

    data class TweetItem(val tweet: Tweet) : DataItem() {
        override val id = tweet.hashCode()
    }

    data class UserInfoItem(val userInfo: UserInfo) : DataItem() {
        override val id = 1
    }
}
