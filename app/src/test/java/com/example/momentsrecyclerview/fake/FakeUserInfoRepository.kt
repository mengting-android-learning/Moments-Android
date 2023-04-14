package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.UserInfoRepository
import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo

class FakeUserInfoRepository : UserInfoRepository {
    override suspend fun getNetworkUserInfo(): NetworkUserInfo = FakeData.networkUserInfo
}
