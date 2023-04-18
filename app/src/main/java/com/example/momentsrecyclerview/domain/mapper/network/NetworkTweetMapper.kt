package com.example.momentsrecyclerview.domain.mapper.network

import com.example.momentsrecyclerview.data.source.network.NetworkImage
import com.example.momentsrecyclerview.data.source.network.NetworkSender
import com.example.momentsrecyclerview.data.source.network.NetworkTweet
import com.example.momentsrecyclerview.data.source.network.NetworkTweetComment
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Sender
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.TweetComment

fun List<NetworkTweet>.asDomainModel() = filter {
    it.sender != null
}.filter {
    !(it.images == null && it.content == null)
}.map {
    it.asDomainModel()
}

fun NetworkTweet.asDomainModel() = Tweet(
    content = content,
    images = images?.map { it.asDomainModel() },
    sender = sender!!.asDomainModel(),
    comments = comments?.map { it.asDomainModel() }
)

fun NetworkTweetComment.asDomainModel() =
    TweetComment(
        content = content,
        sender = sender.asDomainModel()
    )

fun NetworkSender.asDomainModel() =
    Sender(
        userName = username,
        nick = nick,
        avatarUrl = avatarUrl
    )

fun NetworkImage.asDomainModel() = ImageUrl(url = url)
