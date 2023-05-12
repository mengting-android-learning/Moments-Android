package com.example.momentsrecyclerview.github.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val USER_INFO_URL = "https://tw-mobile-xian.github.io/moments-data/"

private val gitRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(USER_INFO_URL)
    .build()

object GitNetwork {
    val service: GitNetworkService by lazy {
        gitRetrofit.create(GitNetworkService::class.java)
    }
}

interface GitNetworkService {

    @GET("tweets.json")
    suspend fun getTweetsList(): List<GitNetworkTweet>

    @GET("user.json")
    suspend fun getUserInfo(): GitNetworkUserInfo
}
