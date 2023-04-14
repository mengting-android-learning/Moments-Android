package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo
import com.example.momentsrecyclerview.data.source.network.NetworkUserInfoService

interface UserInfoRepository {
    suspend fun getNetworkUserInfo(): NetworkUserInfo
}

class NetworkUserInfoRepository(private val networkUserInfoService: NetworkUserInfoService) : UserInfoRepository {
    override suspend fun getNetworkUserInfo(): NetworkUserInfo =
        networkUserInfoService.getUserInfo()
}
