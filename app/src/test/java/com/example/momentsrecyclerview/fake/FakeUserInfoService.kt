package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo
import com.example.momentsrecyclerview.data.source.network.UserInfoService

class FakeUserInfoService : UserInfoService {
    override suspend fun getUserInfo(): NetworkUserInfo = FakeData.networkUserInfo
}
