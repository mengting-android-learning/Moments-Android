package com.example.momentsrecyclerview.domain

import com.example.momentsrecyclerview.data.source.network.NetworkUserInfo

fun NetworkUserInfo.asDomainModel() =
    UserInfo(
        profileImageUrl = profileImageUrl,
        avatarUrl = avatarUrl,
        nick = nick,
        userName = userName
    )
