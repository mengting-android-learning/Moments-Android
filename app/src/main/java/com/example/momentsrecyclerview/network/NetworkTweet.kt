package com.example.momentsrecyclerview.network

import com.example.momentsrecyclerview.data.ImageUrl
import com.example.momentsrecyclerview.data.Sender
import com.example.momentsrecyclerview.data.Tweet
import com.example.momentsrecyclerview.data.TweetComment
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
class NetworkTweetComment(
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

fun List<NetworkTweet>.asDomainModel() = filter {
    it.sender != null
}.filter {
    !(it.images == null && it.content == null)
}.map { networkTweet ->
    Tweet(
        content = networkTweet.content,
        images = networkTweet.images?.map {
            it.asDomainModel()
        },
        sender = networkTweet.sender!!.asDomainModel(),
        comments = networkTweet.comments?.map { it.asDomainModel() }
    )
}

fun NetworkImage.asDomainModel() = ImageUrl(url = this.url)

fun NetworkSender.asDomainModel() =
    Sender(
        userName = username,
        nick = nick,
        avatarUrl = avatarUrl
    )

fun NetworkTweetComment.asDomainModel() =
    TweetComment(
        content = content,
        sender = sender.asDomainModel()
    )
