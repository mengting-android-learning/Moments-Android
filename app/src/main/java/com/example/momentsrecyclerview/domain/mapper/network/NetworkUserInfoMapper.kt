package com.example.momentsrecyclerview.domain.mapper.network

import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo
import com.example.momentsrecyclerview.domain.UserInfo

fun NetworkUserInfo.asDomainModel() =
    UserInfo(
        profileImageUrl = profileImageUrl,
        avatarUrl = avatarUrl,
        nick = nick,
        userName = userName
    )
