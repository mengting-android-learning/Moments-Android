package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.domain.Tweet
import com.example.momentsrecyclerview.data.source.network.asDomainModel
import com.example.momentsrecyclerview.network.TweetsService

interface TweetsRepository {
    suspend fun getTweetsList(): List<Tweet>
}

class NetworkTweetsRepository(private val tweetsService: TweetsService) : TweetsRepository {
    override suspend fun getTweetsList(): List<Tweet> =
        tweetsService.getTweetsList().asDomainModel()
}
