package com.example.momentsrecyclerview.data.source.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkTweet(
    val content: String?,
    val images: List<NetworkImage>?,
    val sender: NetworkSender?,
    val comments: List<NetworkTweetComment>?
)

@JsonClass(generateAdapter = true)
data class NetworkTweetComment(
    val content: String,
    val sender: NetworkSender
)

@JsonClass(generateAdapter = true)
data class NetworkSender(
    val username: String,
    val nick: String,
    @Json(name = "avatar") val avatarUrl: String
)

@JsonClass(generateAdapter = true)
data class NetworkImage(
    val url: String
)
