package com.example.momentsrecyclerview.data.source.network

import retrofit2.http.GET

interface UserInfoService {
    @GET("user.json")
    suspend fun getUserInfo(): NetworkUserInfo
}
