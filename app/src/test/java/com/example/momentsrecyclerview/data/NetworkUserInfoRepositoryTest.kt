package com.example.momentsrecyclerview.data

import com.example.momentsrecyclerview.fake.FakeData
import com.example.momentsrecyclerview.fake.FakeNetworkUserInfoService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkUserInfoRepositoryTest {
    @Test
    fun networkUserInfoRepository_getUserInfo_verifySuccess() {
        runTest {
            val userInfoRepository = NetworkUserInfoRepository(FakeNetworkUserInfoService())
            val userInfo = userInfoRepository.getUserInfo()
            assertEquals(FakeData.networkUserInfo, userInfo)
        }
    }
}
