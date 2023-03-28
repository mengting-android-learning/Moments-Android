package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.TweetsRepository
import com.example.momentsrecyclerview.data.source.network.NetworkTweet

class FakeTweetsRepository : TweetsRepository {
    override suspend fun getTweetsList(): List<NetworkTweet> = listOf(FakeData.networkTweet)
}
