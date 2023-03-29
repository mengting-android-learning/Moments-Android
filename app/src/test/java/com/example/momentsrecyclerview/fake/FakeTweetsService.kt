package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.source.network.NetworkTweet
import com.example.momentsrecyclerview.data.source.network.TweetsService

class FakeTweetsService : TweetsService {
    override suspend fun getTweetsList(): List<NetworkTweet> = listOf(FakeData.networkTweet)
}
