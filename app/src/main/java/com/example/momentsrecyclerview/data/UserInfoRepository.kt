package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo
import com.example.momentsrecyclerview.data.source.network.UserInfoService

interface UserInfoRepository {
    suspend fun getUserInfo(): NetworkUserInfo
}

class NetworkUserInfoRepository(private val userInfoService: UserInfoService) : UserInfoRepository {
    override suspend fun getUserInfo(): NetworkUserInfo =
        userInfoService.getUserInfo()
}
