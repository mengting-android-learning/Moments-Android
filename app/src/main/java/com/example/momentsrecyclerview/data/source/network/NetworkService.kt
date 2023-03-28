package com.example.momentsrecyclerview.data.source.network

import com.example.momentsrecyclerview.network.TweetsService
import com.example.momentsrecyclerview.network.UserInfoService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val USER_INFO_URL = "https://tw-mobile-xian.github.io/moments-data/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(USER_INFO_URL)
    .build()

object UserInfoNetwork {
    val userInfo: UserInfoService by lazy {
        retrofit.create(UserInfoService::class.java)
    }
}

object TweetsListNetwork {
    val tweets: TweetsService by lazy {
        retrofit.create(TweetsService::class.java)
    }
}
