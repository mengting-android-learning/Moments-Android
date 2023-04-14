package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.source.network.NetworkTweet
import com.example.momentsrecyclerview.data.source.network.NetworkTweetsService

class FakeNetworkTweetsService : NetworkTweetsService {
    override suspend fun getTweetsList(): List<NetworkTweet> = listOf(FakeData.networkTweet)
}
