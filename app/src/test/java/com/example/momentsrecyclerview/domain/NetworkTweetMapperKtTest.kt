package com.example.momentsrecyclerview.domain

import com.example.momentsrecyclerview.data.source.network.NetworkImage
import com.example.momentsrecyclerview.data.source.network.NetworkSender
import com.example.momentsrecyclerview.data.source.network.NetworkTweet
import com.example.momentsrecyclerview.data.source.network.NetworkTweetComment
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkTweetMapperKtTest {

    private val networkImage = NetworkImage("http://images/1")
    private val networkSender = NetworkSender("userName", "nick", "http://avatar/1")
    private val networkTweetComment = NetworkTweetComment("comment", networkSender)

    @Test
    fun networkTweetMapper_networkImageUrlAsDomainModel_verifySuccess() {
        val imageUrl = networkImage.asDomainModel()
        assertEquals(imageUrl.url, networkImage.url)
    }

    @Test
    fun networkTweetMapper_networkSenderAsDomainModel_verifySuccess() {
        val sender = networkSender.asDomainModel()
        assertEquals(sender.userName, networkSender.username)
        assertEquals(sender.nick, networkSender.nick)
        assertEquals(sender.avatarUrl, networkSender.avatarUrl)
    }

    @Test
    fun networkTweetMapper_networkTweetCommentAsDomainModel_verifySuccess() {
        val tweetComment = networkTweetComment.asDomainModel()
        assertEquals(tweetComment.content, "comment")
        assertEquals(tweetComment.sender.userName, networkSender.username)
        assertEquals(tweetComment.sender.nick, networkSender.nick)
        assertEquals(tweetComment.sender.avatarUrl, networkSender.avatarUrl)
    }

    @Test
    fun networkTweetMapper_networkTweetsAsDomainModel_verifySuccess() {
        val networkTweet = NetworkTweet("content", listOf(networkImage, networkImage), networkSender, null)
        val networkTweets = listOf(networkTweet)
        val tweets = networkTweets.asDomainModel()
        assertEquals(tweets.size, 1)
        assertEquals(tweets[0].images?.size, 2)
    }

    @Test
    fun networkTweetMapper_networkTweetsAsDomainModelWithNullSender_verifyFilter() {
        val networkTweet = NetworkTweet("content", listOf(networkImage), null, null)
        val networkTweets = listOf(networkTweet)
        val tweets = networkTweets.asDomainModel()
        assertEquals(tweets.size, 0)
    }

    @Test
    fun networkTweetMapper_networkTweetsAsDomainModelWithNullContentAndImages_verifyFilter() {
        val networkTweet = NetworkTweet(null, null, networkSender, null)
        val networkTweets = listOf(networkTweet)
        val tweets = networkTweets.asDomainModel()
        assertEquals(tweets.size, 0)
    }
}
