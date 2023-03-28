package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.network.NetworkTweet
import com.example.momentsrecyclerview.data.source.network.TweetsService

interface TweetsRepository {
    suspend fun getTweetsList(): List<NetworkTweet>
}

class NetworkTweetsRepository(private val tweetsService: TweetsService) : TweetsRepository {
    override suspend fun getTweetsList(): List<NetworkTweet> =
        tweetsService.getTweetsList()
}
