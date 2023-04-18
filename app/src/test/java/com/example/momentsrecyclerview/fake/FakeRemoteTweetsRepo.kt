package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.TweetsRepository
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.mapper.network.asDomainModel

class FakeRemoteTweetsRepo : TweetsRepository {
    override suspend fun getTweetsList(): List<Tweet> =
        listOf(FakeData.networkTweet).asDomainModel()
}

class FakeLocalTweetsRepo : TweetsRepository {
    override suspend fun getTweetsList(): List<Tweet> =
        listOf(FakeData.tweet)
}
