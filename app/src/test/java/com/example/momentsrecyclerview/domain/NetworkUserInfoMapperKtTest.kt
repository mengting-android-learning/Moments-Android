package com.example.momentsrecyclerview.domain

import com.example.momentsrecyclerview.fake.FakeData
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkUserInfoMapperKtTest {

    @Test
    fun networkUserInfoMapper_asDomainModel_verifySuccess() {
        val userInfo = FakeData.networkUserInfo.asDomainModel()
        assertEquals(userInfo, FakeData.userInfo)
    }
}
