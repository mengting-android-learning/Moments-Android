package com.example.momentsrecyclerview.data.source.network.request

import com.example.momentsrecyclerview.data.source.network.NetworkImage
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewTweetRequest(
    val userId: Long,

    val content: String? = null,

    val createdOn: Long,

    val images: List<NetworkImage>? = null
)
