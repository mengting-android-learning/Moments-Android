package com.example.momentsrecyclerview.domain

import com.example.momentsrecyclerview.domain.mapper.network.asDomainModel
import com.example.momentsrecyclerview.fake.FakeData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkTweetMapperKtTest {

    @Test
    fun networkTweetMapper_networkImageUrlAsDomainModel_verifySuccess() {
        val imageUrl = FakeData.networkImage.asDomainModel()
        assertEquals(imageUrl, FakeData.image)
    }

    @Test
    fun networkTweetMapper_networkSenderAsDomainModel_verifySuccess() {
        val sender = FakeData.networkSender.asDomainModel()
        assertEquals(sender, FakeData.sender)
    }

    @Test
    fun networkTweetMapper_networkTweetCommentAsDomainModel_verifySuccess() {
        val tweetComment = FakeData.networkTweetComment.asDomainModel()
        assertEquals(tweetComment, FakeData.tweetComment)
    }

    @Test
    fun networkTweetMapper_networkTweetsAsDomainModel_verifySuccess() {
        val networkTweets = listOf(FakeData.networkTweet)
        val tweets = networkTweets.asDomainModel()
        assertEquals(tweets.size, 1)
        assertEquals(tweets[0], FakeData.tweet)
    }

    @Test
    fun networkTweetMapper_networkTweetsAsDomainModelWithNullSender_verifyFilter() {
        val networkTweets = listOf(FakeData.networkTweet.copy(sender = null))
        val tweets = networkTweets.asDomainModel()
        assertTrue(tweets.isEmpty())
    }

    @Test
    fun networkTweetMapper_networkTweetsAsDomainModelWithNullContentAndImages_verifyFilter() {
        val networkTweets = listOf(FakeData.networkTweet.copy(content = null, images = null))
        val tweets = networkTweets.asDomainModel()
        assertTrue(tweets.isEmpty())
    }
}
