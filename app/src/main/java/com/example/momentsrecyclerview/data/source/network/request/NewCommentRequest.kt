package com.example.momentsrecyclerview.data.source.network.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewCommentRequest(
    val senderId: Long,
    val tweetId: Long,
    val createdOn: Long,
    val content: String,
)
