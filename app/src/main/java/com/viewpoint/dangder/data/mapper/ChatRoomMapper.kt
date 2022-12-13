package com.viewpoint.dangder.data.mapper

import com.viewpoint.FetchChatRoomQuery
import com.viewpoint.FetchChatRoomsQuery
import com.viewpoint.JoinChatRoomMutation
import com.viewpoint.dangder.domain.entity.ChatRoom

object ChatRoomMapper {
    fun mapToChatRoomEntity(chatRoomData: JoinChatRoomMutation.JoinChatRoom) = ChatRoom(
        id = chatRoomData.id,
        pairDogId = chatRoomData.chatPairId,
    )

    fun mapToChatRoomEntity(chatRoomData : FetchChatRoomQuery.FetchChatRoom) = ChatRoom(
        id = chatRoomData.id,
        pairDogId = chatRoomData.chatPairId,
        dog = DogMapper.mapToDogEntity(chatRoomData.dog)
    )

    fun mapToChatRoomEntity(chatRoomData : FetchChatRoomsQuery.FetchChatRoom) = ChatRoom(
        id = chatRoomData.id!!,
        pairDogId = chatRoomData.chatPairDog!!.id,
        chatMessages = chatRoomData.lastMessage?.let {
            listOf(ChatMessageMapper.mapToChatMessageEntity(it))
        }
    )
}