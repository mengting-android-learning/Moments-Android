package com.example.momentsrecyclerview.github.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GitNetworkUserInfo(
    @Json(name = "profile-image") val profileImageUrl: String,
    @Json(name = "avatar") val avatarUrl: String,
    val nick: String,
    @Json(name = "username") val userName: String
)
