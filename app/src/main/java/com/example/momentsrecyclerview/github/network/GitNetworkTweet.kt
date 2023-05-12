package com.example.momentsrecyclerview.github.network

import com.example.momentsrecyclerview.data.source.network.NetworkImage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitNetworkTweet(
    val content: String?,
    val images: List<NetworkImage>?,
    val sender: GitNetworkSender?,
    val comments: List<GitNetworkTweetComment>?
)

@JsonClass(generateAdapter = true)
data class GitNetworkTweetComment(
    val content: String,
    val sender: GitNetworkSender
)

@JsonClass(generateAdapter = true)
data class GitNetworkSender(
    @Json(name = "username") val userName: String,
    val nick: String,
    @Json(name = "avatar") val avatarUrl: String
)

@JsonClass(generateAdapter = true)
data class GitNetworkImage(
    val url: String
)
