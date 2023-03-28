package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.source.network.NetworkImage
import com.example.momentsrecyclerview.data.source.network.NetworkSender
import com.example.momentsrecyclerview.data.source.network.NetworkTweet
import com.example.momentsrecyclerview.data.source.network.NetworkTweetComment
import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Sender
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.TweetComment
import com.example.momentsrecyclerview.domain.UserInfo

object FakeData {
    private const val userName = "user"
    private const val nick = "nick"
    private const val avatarUrl = "http://avatar/1"
    private const val imageUrl = "http://images/1"
    private const val profileUrl = "http://profile/1"
    private const val content = "content"

    val networkImage = NetworkImage(imageUrl)
    val networkSender = NetworkSender(userName, nick, avatarUrl)
    val networkTweetComment = NetworkTweetComment(content, networkSender)
    val networkTweet =
        NetworkTweet(content, listOf(networkImage), networkSender, listOf(networkTweetComment))
    val networkUserInfo = NetworkUserInfo(profileUrl, avatarUrl, nick, userName)

    val image = ImageUrl(imageUrl)
    val sender = Sender(userName, nick, avatarUrl)
    val tweetComment = TweetComment(content, sender)
    val tweet =
        Tweet(content, listOf(image), sender, listOf(tweetComment))
    val userInfo = UserInfo(profileUrl, avatarUrl, nick, userName)
}
