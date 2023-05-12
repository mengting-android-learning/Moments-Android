package com.example.momentsrecyclerview.data.source.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewUserRequest(
    val profileImage: String?,

    val avatar: String,

    val nick: String,

    val userName: String
)
