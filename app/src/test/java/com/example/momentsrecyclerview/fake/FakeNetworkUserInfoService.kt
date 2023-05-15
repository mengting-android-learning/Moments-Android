package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo
import com.example.momentsrecyclerview.data.source.network.NetworkUserInfoService
import com.example.momentsrecyclerview.data.source.network.request.NewUserRequest

class FakeNetworkUserInfoService : NetworkUserInfoService {
    override suspend fun getUserInfo(): NetworkUserInfo =
        FakeData.networkUserInfo


    override suspend fun saveUser(newUser: NewUserRequest): NetworkUserInfo =
        FakeData.networkUserInfo

}
