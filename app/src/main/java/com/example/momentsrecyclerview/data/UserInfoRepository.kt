package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo
import com.example.momentsrecyclerview.data.source.network.NetworkUserInfoService

interface UserInfoRepository {
    suspend fun getUserInfo(): NetworkUserInfo
}

class NetworkUserInfoRepository(private val networkUserInfoService: NetworkUserInfoService) : UserInfoRepository {
    override suspend fun getUserInfo(): NetworkUserInfo =
        networkUserInfoService.getUserInfo()
}
