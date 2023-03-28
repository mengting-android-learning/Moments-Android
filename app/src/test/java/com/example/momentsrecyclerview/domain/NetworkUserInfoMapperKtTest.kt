package com.example.momentsrecyclerview.domain

import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkUserInfoMapperKtTest {

    @Test
    fun networkUserInfoMapper_asDomainModel_verifySuccess() {
        val networkUserInfo = NetworkUserInfo("http://profile/1", "http://avatar/1", "nick", "user")
        val userInfo = networkUserInfo.asDomainModel()
        assertEquals(userInfo.userName, networkUserInfo.userName)
        assertEquals(userInfo.avatarUrl, networkUserInfo.avatarUrl)
        assertEquals(userInfo.nick, networkUserInfo.nick)
        assertEquals(userInfo.profileImageUrl, networkUserInfo.profileImageUrl)
    }
}
