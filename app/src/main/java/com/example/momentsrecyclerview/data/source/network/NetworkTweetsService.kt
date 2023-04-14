package com.example.momentsrecyclerview.data.source.network

import retrofit2.http.GET

interface NetworkTweetsService {
    @GET("tweets.json")
    suspend fun getTweetsList(): List<NetworkTweet>
}
