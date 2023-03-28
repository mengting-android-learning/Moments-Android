package com.example.momentsrecyclerview.data.source.network

import com.example.momentsrecyclerview.data.domain.UserInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkUserInfo(
    @Json(name = "profile-image") val profileImageUrl: String,
    @Json(name = "avatar") val avatarUrl: String,
    val nick: String,
    @Json(name = "username") val userName: String
)

fun NetworkUserInfo.asDomainModel() =
    UserInfo(
        profileImageUrl = profileImageUrl,
        avatarUrl = avatarUrl,
        nick = nick,
        userName = userName
    )
