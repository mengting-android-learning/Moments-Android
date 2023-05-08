package com.example.momentsrecyclerview.data.source.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkUserInfo(
    val id: Long,
    @Json(name = "profileImage") val profileImageUrl: String?,
    @Json(name = "avatar") val avatarUrl: String,
    val nick: String,
    val userName: String
)
