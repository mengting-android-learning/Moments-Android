package com.example.momentsrecyclerview.data.source.network

import retrofit2.http.GET

interface NetworkUserInfoService {
//    @GET("user.json")
    @GET("users?name=hengzeng")
    suspend fun getUserInfo(): NetworkUserInfo
}
