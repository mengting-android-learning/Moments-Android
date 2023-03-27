package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.network.TweetsListService
import com.example.momentsrecyclerview.data.source.network.asDomainModel

interface TweetsRepository {
    suspend fun getTweetsList(): List<Tweet>
}

class NetworkTweetsRepository(private val tweetsListService: TweetsListService) : TweetsRepository {
    override suspend fun getTweetsList(): List<Tweet> =
        tweetsListService.getTweetsList().asDomainModel()
}
