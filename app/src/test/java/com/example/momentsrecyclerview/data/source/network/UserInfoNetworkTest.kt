package com.example.momentsrecyclerview.data.source.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class UserInfoNetworkTest {

    private lateinit var retrofit: Retrofit

    @Before
    fun setup() {
        val url = "https://tw-mobile-xian.github.io/moments-data/"
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(url)
            .build()
    }

    @Test
    fun networkService_userInfoService_verifySuccess() {
        runBlocking {
            val userInfoResponse = retrofit.create(UserInfoService::class.java).getUserInfo()
            assertNotNull(userInfoResponse)
        }
    }

    @Test
    fun networkService_tweetsService_verifySuccess() {
        runBlocking {
            val tweetsResponse = retrofit.create(TweetsService::class.java).getTweetsList()
            assertNotNull(tweetsResponse)
        }
    }
}
