package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo
import com.example.momentsrecyclerview.data.source.network.NetworkUserInfoService

class FakeNetworkUserInfoService : NetworkUserInfoService {
    override suspend fun getUserInfo(): NetworkUserInfo = FakeData.networkUserInfo
}
