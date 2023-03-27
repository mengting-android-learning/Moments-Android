package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.network.UserInfoService
import com.example.momentsrecyclerview.data.source.network.asDomainModel

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfo
}

class NetworkUserInfoRepository(private val userInfoService: UserInfoService) : UserInfoRepository {
    override suspend fun getUserInfo(): UserInfo =
        userInfoService.getUserInfo().asDomainModel()
}