package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.UserInfoRepository
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.domain.mapper.network.asDomainModel

class FakeRemoteUserInfoRepo : UserInfoRepository {
    override suspend fun getUserInfo(): UserInfo = FakeData.networkUserInfo.asDomainModel()
    override suspend fun saveUserInfo(userInfo: UserInfo) {}
}

class FakeLocalUserInfoRepo : UserInfoRepository {
    override suspend fun getUserInfo(): UserInfo = FakeData.userInfo
    override suspend fun saveUserInfo(userInfo: UserInfo) {}
}
