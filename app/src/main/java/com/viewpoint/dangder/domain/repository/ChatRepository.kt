package com.viewpoint.dangder.domain.repository

import com.viewpoint.dangder.domain.entity.ChatRoom

interface ChatRepository {
    suspend fun joinChatRoom(myDogId : String, pairDogId : String) : ChatRoom
}