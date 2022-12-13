package com.viewpoint.dangder.domain.entity

data class ChatRoom(
    val id : String,
    val pairDogId : String,
    val dog : Dog? = null,
    val chatMessages : List<ChatMessage>? = emptyList()
)
