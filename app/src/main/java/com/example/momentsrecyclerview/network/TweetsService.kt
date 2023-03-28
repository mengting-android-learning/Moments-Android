package com.example.momentsrecyclerview.network

import com.example.momentsrecyclerview.data.source.network.NetworkTweet
import retrofit2.http.GET

interface TweetsService {
    @GET("tweets.json")
    suspend fun getTweetsList(): List<NetworkTweet>
}
