package com.example.momentsrecyclerview.data.source.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val USER_INFO_URL = "https://tw-mobile-xian.github.io/moments-data/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(USER_INFO_URL)
    .build()

object UserInfoNetwork {
    val userInfo: NetworkUserInfoService by lazy {
        retrofit.create(NetworkUserInfoService::class.java)
    }
}

object TweetsListNetwork {
    val tweets: NetworkTweetsService by lazy {
        retrofit.create(NetworkTweetsService::class.java)
    }
}
