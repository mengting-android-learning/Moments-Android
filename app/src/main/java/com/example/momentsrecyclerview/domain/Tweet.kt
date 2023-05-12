package com.example.momentsrecyclerview.domain

data class Tweet(
    val id: Long = 0L,
    val content: String? = null,
    val createdOn: Long,
    val images: List<ImageUrl>? = null,
    val sender: Sender,
    val comments: List<TweetComment>? = null
)

data class TweetComment(
    val id: Long,
    val content: String,
    val createdOn: Long,
    val sender: Sender
)

data class Sender(
    val id: Long,
    val userName: String,
    val nick: String,
    val avatarUrl: String
)

data class ImageUrl(val url: String)
