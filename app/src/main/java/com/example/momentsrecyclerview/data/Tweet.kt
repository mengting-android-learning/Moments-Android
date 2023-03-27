package com.example.momentsrecyclerview.data

data class Tweet(
    val content: String?,
    val images: List<ImageUrl>?,
    val sender: Sender,
    val comments: List<TweetComment>?
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
