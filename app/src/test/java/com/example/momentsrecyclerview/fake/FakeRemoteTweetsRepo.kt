package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.TweetsRepository
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.mapper.network.asDomainModel

class FakeRemoteTweetsRepo : TweetsRepository {
    override suspend fun getTweetsList(): List<Tweet> =
        listOf(FakeData.networkTweet).asDomainModel()

    override suspend fun saveNewTweet(tweet: Tweet) {
        throw Exception()
    }
}

class FakeLocalTweetsRepo : TweetsRepository {
    override suspend fun getTweetsList(): List<Tweet> =
        listOf(FakeData.tweet)

    override suspend fun saveNewTweet(tweet: Tweet) {
    }
}
