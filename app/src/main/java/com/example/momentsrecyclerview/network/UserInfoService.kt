package com.example.momentsrecyclerview.network

import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo
import retrofit2.http.GET

interface UserInfoService {
    @GET("user.json")
    suspend fun getUserInfo(): NetworkUserInfo
}
