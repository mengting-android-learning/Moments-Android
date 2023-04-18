package com.example.momentsrecyclerview.fake

import com.example.momentsrecyclerview.data.UserInfoRepository
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.domain.mapper.network.asDomainModel

class FakeUserInfoRepository : UserInfoRepository {
    override suspend fun getUserInfo(): UserInfo = FakeData.networkUserInfo.asDomainModel()
}
