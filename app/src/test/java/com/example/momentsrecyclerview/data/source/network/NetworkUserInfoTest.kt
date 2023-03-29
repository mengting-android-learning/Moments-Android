package com.example.momentsrecyclerview.data.source.network

import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkUserInfoTest {
    @Test
    fun userInfoNetwork_deserialization_verifySuccess() {
        val json = """{
                        "profile-image": "https://profile-image.jpeg",
                        "avatar": "https:///avatar.png",
                        "nick": "nick",
                        "username": "user"
                    }"""
        val networkUserInfo =
            NetworkUserInfo("https://profile-image.jpeg", "https:///avatar.png", "nick", "user")
        val moshi = Moshi.Builder().build()
        val userInfo = moshi.adapter(NetworkUserInfo::class.java).fromJson(json)
        assertEquals(networkUserInfo, userInfo)
    }
}
