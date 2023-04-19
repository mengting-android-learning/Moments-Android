package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.local.MomentsDatabaseDao
import com.example.momentsrecyclerview.data.source.network.NetworkTweetsService
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.mapper.local.asDomainTweet
import com.example.momentsrecyclerview.domain.mapper.local.toLocalImage
import com.example.momentsrecyclerview.domain.mapper.local.toLocalTweet
import com.example.momentsrecyclerview.domain.mapper.local.toLocalTweetComment
import com.example.momentsrecyclerview.domain.mapper.local.toLocalUser
import com.example.momentsrecyclerview.domain.mapper.network.asDomainModel

interface TweetsRepository {
    suspend fun getTweetsList(): List<Tweet>
}

class NetworkTweetsRepository(private val networkTweetsService: NetworkTweetsService) :
    TweetsRepository {
    override suspend fun getTweetsList(): List<Tweet> =
        networkTweetsService.getTweetsList().asDomainModel()
}

class LocalTweetsRepository(private val dataSource: MomentsDatabaseDao) : TweetsRepository {
    override suspend fun getTweetsList(): List<Tweet> =
        dataSource.loadTweets().map { it.asDomainTweet() }

    suspend fun saveTweets(tweets: List<Tweet>) {
        val localUserInfo = dataSource.getUserInfo()
        dataSource.deleteTweets()
        dataSource.deleteComments()
        dataSource.deleteImages()
        dataSource.deleteUsers()
        if (localUserInfo != null) dataSource.insertUser(localUserInfo)
        for (tweet in tweets) {
            val senderId = dataSource.insertUser(tweet.sender.toLocalUser())
            val tweetId = dataSource.insertTweet(tweet.toLocalTweet(senderId))
            if (tweet.comments?.isNotEmpty() == true) {
                for (comment in tweet.comments) {
                    val commentSenderId = dataSource.insertUser(comment.sender.toLocalUser())
                    dataSource.insertTweetComment(
                        comment.toLocalTweetComment(
                            commentSenderId,
                            tweetId
                        )
                    )
                }
            }
            if (tweet.images?.isNotEmpty() == true) {
                dataSource.insertImage(tweet.images.map { it.toLocalImage(tweetId) })
            }
        }
    }
}
