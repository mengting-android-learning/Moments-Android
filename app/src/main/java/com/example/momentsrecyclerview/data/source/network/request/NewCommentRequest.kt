package com.example.momentsrecyclerview.data.source.network.request

data class NewCommentRequest(
    private val senderId: Long,
    private val tweetId: Long,
    private val content: Long
)
