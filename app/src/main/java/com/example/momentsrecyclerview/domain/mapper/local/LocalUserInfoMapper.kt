package com.example.momentsrecyclerview.domain.mapper.local

import com.example.momentsrecyclerview.data.source.local.LocalUser
import com.example.momentsrecyclerview.domain.UserInfo

fun LocalUser.asDomainUserInfo() = profileImageUrl?.let {
    UserInfo(
        userName = userName,
        nick = nick,
        avatarUrl = avatarUrl,
        profileImageUrl = it
    )
}

fun UserInfo.toLocalUser() = LocalUser(
    userName = userName,
    nick = nick,
    avatarUrl = avatarUrl,
    profileImageUrl = profileImageUrl
)
