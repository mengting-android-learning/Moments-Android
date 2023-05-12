package com.example.momentsrecyclerview.domain.mapper.local

import com.example.momentsrecyclerview.data.source.local.CommentWithSender
import com.example.momentsrecyclerview.data.source.local.LocalEntireTweet
import com.example.momentsrecyclerview.data.source.local.LocalImage
import com.example.momentsrecyclerview.data.source.local.LocalTweet
import com.example.momentsrecyclerview.data.source.local.LocalTweetComment
import com.example.momentsrecyclerview.data.source.local.LocalUser
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Sender
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.TweetComment

fun LocalEntireTweet.asDomainTweet() = Tweet(
    id = tweet.id,
    content = tweet.content,
    createdOn = tweet.createdOn,
    images =
    if (images?.isNotEmpty() == true) images.map { it.asDomainImage() } else null,
    sender = sender.asDomainSender(),
    comments = if (comments?.isNotEmpty() == true) comments.map { it.asDomainComment() } else null
)

fun CommentWithSender.asDomainComment() = TweetComment(
    id = comment.id,
    content = comment.content,
    createdOn = comment.createdOn,
    sender = sender.asDomainSender()
)

fun LocalUser.asDomainSender() = Sender(
    id = userId,
    userName = userName,
    nick = nick,
    avatarUrl = avatarUrl
)

fun LocalImage.asDomainImage() = ImageUrl(url = imageUrl)

fun Tweet.toLocalTweet(senderId: Long, isOnlyLocal: Boolean = false) = LocalTweet(
    id = id,
    content = content,
    createdOn = createdOn,
    senderId = senderId,
    isOnlyLocal = isOnlyLocal
)

fun TweetComment.toLocalTweetComment(senderId: Long, tweetId: Long) = LocalTweetComment(
    id = id,
    content = content,
    createdOn = createdOn,
    senderId = senderId,
    tweetId = tweetId
)

fun Sender.toLocalUser() = LocalUser(
    userId = id,
    userName = userName,
    nick = nick,
    avatarUrl = avatarUrl
)

fun ImageUrl.toLocalImage(id: Long) = LocalImage(imageUrl = url, tweetId = id)
