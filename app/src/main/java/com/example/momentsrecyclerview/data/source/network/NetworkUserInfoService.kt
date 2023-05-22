package com.example.momentsrecyclerview.data.source.network

import com.example.momentsrecyclerview.data.source.network.request.NewUserRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface NetworkUserInfoService {
//    @GET("user.json")
    @GET("users?name=hengzeng")
    suspend fun getUserInfo(): NetworkUserInfo

    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun saveUser(@Body newUser: NewUserRequest): NetworkUserInfo
}
