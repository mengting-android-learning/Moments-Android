package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.network.NetworkTweet
import com.example.momentsrecyclerview.data.source.network.NetworkTweetsService

interface TweetsRepository {
    suspend fun getTweetsList(): List<NetworkTweet>
}

class NetworkTweetsRepository(private val networkTweetsService: NetworkTweetsService) : TweetsRepository {
    override suspend fun getTweetsList(): List<NetworkTweet> =
        networkTweetsService.getTweetsList()
}
