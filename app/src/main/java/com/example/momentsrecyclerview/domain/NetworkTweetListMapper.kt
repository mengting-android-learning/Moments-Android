package com.example.momentsrecyclerview.domain

import com.example.momentsrecyclerview.data.source.network.NetworkImage
import com.example.momentsrecyclerview.data.source.network.NetworkSender
import com.example.momentsrecyclerview.data.source.network.NetworkTweet
import com.example.momentsrecyclerview.data.source.network.NetworkTweetComment

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

fun NetworkImage.asDomainModel() = ImageUrl(url = url)

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
