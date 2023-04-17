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

fun List<LocalEntireTweet>.asDomain() = map {
    Tweet(
        content = it.tweet.content,
        images = it.images?.asDomainImages(),
        sender = it.sender.asDomainSender(),
        comments = it.comments?.asDomainComments()
    )
}

fun List<CommentWithSender>.asDomainComments() = if (isNotEmpty()) {
    map {
        TweetComment(
            content = it.comment.content,
            sender = it.sender.asDomainSender()
        )
    }
} else {
    null
}

fun LocalUser.asDomainSender() = Sender(
    userName = userName,
    nick = nick,
    avatarUrl = avatarUrl
)

fun List<LocalImage>.asDomainImages() = if (isNotEmpty()) {
    map {
        ImageUrl(url = it.imageUrl)
    }
} else {
    null
}

fun Tweet.toLocalTweet(senderId: Long) = LocalTweet(
    content = content,
    senderId = senderId
)

fun TweetComment.toLocalTweetComment(senderId: Long, tweetId: Long) = LocalTweetComment(
    content = content,
    senderId = senderId,
    tweetId = tweetId
)

fun Sender.toLocalUser() = LocalUser(
    userName = userName,
    nick = nick,
    avatarUrl = avatarUrl
)

fun List<ImageUrl>.toLocalImage(id: Long) = map {
    LocalImage(imageUrl = it.url, tweetId = id)
}
