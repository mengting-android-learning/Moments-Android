package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.data.source.local.MomentsDatabaseDao
import com.example.momentsrecyclerview.data.source.network.NetworkUserInfoService
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.domain.mapper.local.asDomainUserInfo
import com.example.momentsrecyclerview.domain.mapper.local.toLocalUser
import com.example.momentsrecyclerview.domain.mapper.network.asDomainModel

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfo?
    suspend fun saveUserInfo(userInfo: UserInfo)
}

class NetworkUserInfoRepository(private val networkUserInfoService: NetworkUserInfoService) :
    UserInfoRepository {
    override suspend fun getUserInfo(): UserInfo =
        networkUserInfoService.getUserInfo().asDomainModel()

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        // push update userInfo to remote
    }
}

class LocalUserInfoRepository(private val dataSource: MomentsDatabaseDao) : UserInfoRepository {
    override suspend fun getUserInfo(): UserInfo? =
        dataSource.getUserInfo()?.asDomainUserInfo()

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        val localUserInfo = dataSource.getUserInfo()
        if (localUserInfo != null) {
            dataSource.updateUser(userInfo.toLocalUser().copy(userId = localUserInfo.userId))
        } else {
            dataSource.insertUser(userInfo.toLocalUser())
        }
    }
}
