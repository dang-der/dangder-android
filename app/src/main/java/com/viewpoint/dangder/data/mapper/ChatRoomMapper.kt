package com.viewpoint.dangder.data.mapper

import com.viewpoint.JoinChatRoomMutation
import com.viewpoint.dangder.domain.entity.ChatRoom

object ChatRoomMapper {
    fun mapToChatRoomEntity(chatRoomData: JoinChatRoomMutation.JoinChatRoom) = ChatRoom(
        id = chatRoomData.id,
        pairDogId = chatRoomData.chatPairId,
        chatMessages = chatRoomData.chatMessages.map {
            ChatMessageMapper.mapToChatMessage(it)
        }
    )
}