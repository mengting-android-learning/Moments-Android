package com.example.momentsrecyclerview.data.source.network.request

data class NewUserRequest(
    private val profileImage: String? = null,

    private val avatar: String,

    private val nick: String,

    private val userName: String
)
