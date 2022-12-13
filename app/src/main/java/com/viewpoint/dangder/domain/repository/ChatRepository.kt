package com.viewpoint.dangder.domain.repository

import com.viewpoint.dangder.domain.entity.ChatMessage
import com.viewpoint.dangder.domain.entity.ChatRoom

interface ChatRepository {
    suspend fun joinChatRoom(myDogId : String, pairDogId : String) : ChatRoom
    suspend fun fetchChatRoom(roomId : String) : ChatRoom
    suspend fun fetchChatMessagesByChatRoomId(roomId: String) : List<ChatMessage>
    suspend fun fetchChatRooms(dogId : String) : List<ChatRoom>
}