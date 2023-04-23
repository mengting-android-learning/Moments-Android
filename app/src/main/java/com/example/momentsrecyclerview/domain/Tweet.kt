package com.example.momentsrecyclerview.domain

data class Tweet(
    val content: String? = null,
    val images: List<ImageUrl>? = null,
    val sender: Sender,
    val comments: List<TweetComment>? = null
)

data class TweetComment(
    val content: String,
    val sender: Sender
)

data class Sender(
    val userName: String,
    val nick: String,
    val avatarUrl: String
)

data class ImageUrl(val url: String)
