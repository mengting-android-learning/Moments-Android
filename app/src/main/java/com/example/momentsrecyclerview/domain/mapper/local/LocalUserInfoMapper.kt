package com.example.momentsrecyclerview.domain.mapper.local

import com.example.momentsrecyclerview.data.source.local.LocalUser
import com.example.momentsrecyclerview.domain.UserInfo

fun LocalUser.asDomainUserInfo() = UserInfo(
    id = userId,
    userName = userName,
    nick = nick,
    avatarUrl = avatarUrl,
    profileImageUrl = profileImageUrl!!
)

fun UserInfo.toLocalUser() = LocalUser(
    userId = id,
    userName = userName,
    nick = nick,
    avatarUrl = avatarUrl,
    profileImageUrl = profileImageUrl
)
