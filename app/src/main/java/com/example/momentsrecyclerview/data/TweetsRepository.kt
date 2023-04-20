package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.local.MomentsDatabaseDao
import com.example.momentsrecyclerview.data.source.network.NetworkTweetsService
import com.example.momentsrecyclerview.domain.Sender
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.mapper.local.asDomainTweet
import com.example.momentsrecyclerview.domain.mapper.local.toLocalImage
import com.example.momentsrecyclerview.domain.mapper.local.toLocalTweet
import com.example.momentsrecyclerview.domain.mapper.local.toLocalTweetComment
import com.example.momentsrecyclerview.domain.mapper.local.toLocalUser
import com.example.momentsrecyclerview.domain.mapper.network.asDomainModel

interface TweetsRepository {
    suspend fun getTweetsList(): List<Tweet>
    suspend fun saveNewTweet(tweet: Tweet)
}

class NetworkTweetsRepository(private val networkTweetsService: NetworkTweetsService) :
    TweetsRepository {
    override suspend fun getTweetsList(): List<Tweet> =
        networkTweetsService.getTweetsList().asDomainModel()

    override suspend fun saveNewTweet(tweet: Tweet) {
        throw Exception()
    }
}

class LocalTweetsRepository(private val dataSource: MomentsDatabaseDao) : TweetsRepository {
    override suspend fun getTweetsList(): List<Tweet> =
        dataSource.loadTweets().asReversed().map { it.asDomainTweet() }

    override suspend fun saveNewTweet(tweet: Tweet) = insertEntireTweet(tweet)

    suspend fun saveTweets(tweets: List<Tweet>) {
        val localUserInfo = dataSource.getUserInfo()
        clearAllData()
        if (localUserInfo != null) dataSource.insertUser(localUserInfo)
        for (tweet in tweets.asReversed()) {
            insertEntireTweet(tweet)
        }
    }

    private suspend fun insertEntireTweet(
        tweet: Tweet
    ) {
        val senderId = insertUser(tweet.sender)
        val tweetId = insertTweet(tweet, senderId)
        insertComments(tweet, tweetId)
        insertImages(tweet, tweetId)
    }

    private suspend fun insertComments(
        tweet: Tweet,
        tweetId: Long
    ) {
        if (tweet.comments?.isNotEmpty() == true) {
            for (comment in tweet.comments) {
                val commentSenderId = insertUser(comment.sender)
                dataSource.insertTweetComment(
                    comment.toLocalTweetComment(
                        commentSenderId,
                        tweetId
                    )
                )
            }
        }
    }

    private suspend fun insertImages(
        tweet: Tweet,
        tweetId: Long
    ) {
        if (tweet.images?.isNotEmpty() == true) {
            dataSource.insertImage(tweet.images.map { it.toLocalImage(tweetId) })
        }
    }

    private suspend fun insertTweet(
        tweet: Tweet,
        senderId: Long
    ) = dataSource.insertTweet(tweet.toLocalTweet(senderId))

    private suspend fun insertUser(sender: Sender) =
        dataSource.insertUser(sender.toLocalUser())

    private suspend fun clearAllData() {
        dataSource.deleteTweets()
        dataSource.deleteComments()
        dataSource.deleteImages()
        dataSource.deleteUsers()
    }
}
