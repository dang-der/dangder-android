package com.viewpoint.dangder.domain.entity

data class ChatRoom(
    val id : String,
    val pairDogId : String,
    val pairDog : Dog? = null,
    val dog : Dog? = null,
    val chatMessages : List<ChatMessage>? = emptyList()
)
