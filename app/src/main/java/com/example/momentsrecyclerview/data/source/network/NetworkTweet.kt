package com.example.momentsrecyclerview.data.source.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkTweet(
    val id: Long,
    val content: String?,
    val createdOn: Long,
    val images: List<NetworkImage>?,
    val sender: NetworkSender?,
    val comments: List<NetworkTweetComment>?
)

@JsonClass(generateAdapter = true)
data class NetworkTweetComment(
    val id: Long,
    val content: String,
    val createdOn: Long,
    val sender: NetworkSender
)

@JsonClass(generateAdapter = true)
data class NetworkSender(
    val id: Long,
    val userName: String,
    val nick: String,
    @Json(name = "avatar") val avatarUrl: String
)

@JsonClass(generateAdapter = true)
data class NetworkImage(
    val url: String
)
