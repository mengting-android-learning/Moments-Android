package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.network.NetworkTweet
import com.example.momentsrecyclerview.data.source.network.NetworkTweetsService

interface TweetsRepository {
    suspend fun getNetworkTweetsList(): List<NetworkTweet>
}

class NetworkTweetsRepository(private val networkTweetsService: NetworkTweetsService) : TweetsRepository {
    override suspend fun getNetworkTweetsList(): List<NetworkTweet> =
        networkTweetsService.getTweetsList()
}
