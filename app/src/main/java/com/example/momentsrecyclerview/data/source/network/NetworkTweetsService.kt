package com.example.momentsrecyclerview.data.source.network

import com.example.momentsrecyclerview.data.source.network.request.NewCommentRequest
import com.example.momentsrecyclerview.data.source.network.request.NewTweetRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface NetworkTweetsService {
    //    @GET("tweets.json")
    @GET("tweets")
    suspend fun getTweetsList(): List<NetworkTweet>

    @Headers("Content-Type: application/json")
    @POST("tweets")
    suspend fun saveNewTweet(@Body newTweet: NewTweetRequest): NetworkTweet

    @Headers("Content-Type: application/json")
    @POST("tweets/comments")
    suspend fun saveNewComment(@Body newComment:NewCommentRequest):NetworkTweetComment
}
