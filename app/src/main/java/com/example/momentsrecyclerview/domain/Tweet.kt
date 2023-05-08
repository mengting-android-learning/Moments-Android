package com.example.momentsrecyclerview.domain

import java.sql.Timestamp

data class Tweet(
    val id: Long,
    val content: String? = null,
    val createdOn: Timestamp,
    val images: List<ImageUrl>? = null,
    val sender: Sender,
    val comments: List<TweetComment>? = null
)

data class TweetComment(
    val id: Long,
    val content: String,
    val createdOn: Timestamp,
    val sender: Sender
)

data class Sender(
    val id: Long,
    val userName: String,
    val nick: String,
    val avatarUrl: String
)

data class ImageUrl(val url: String)
