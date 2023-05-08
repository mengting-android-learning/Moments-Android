package com.example.momentsrecyclerview.data.source.network.request

import com.example.momentsrecyclerview.data.source.network.NetworkImage

data class NewTweetRequest(
    private val userId: Long,

    private val content: String? = null,

    private val images: List<NetworkImage>? = null
)
