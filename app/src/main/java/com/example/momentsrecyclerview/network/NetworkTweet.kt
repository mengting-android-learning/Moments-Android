package com.example.momentsrecyclerview.network

import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Sender
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.TweetComment
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

fun NetworkImage.asDomainModel() = let {
    ImageUrl(url = it.url)
}

fun NetworkSender.asDomainModel() = let {
    Sender(
        userName = it.username,
        nick = it.nick,
        avatarUrl = it.avatarUrl
    )
}

fun NetworkTweetComment.asDomainModel() = let {
    TweetComment(
        content = it.content,
        sender = it.sender.asDomainModel()
    )
}
