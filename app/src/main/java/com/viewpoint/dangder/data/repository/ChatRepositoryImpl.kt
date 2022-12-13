package com.viewpoint.dangder.data.repository

import com.viewpoint.dangder.data.mapper.ChatMessageMapper
import com.viewpoint.dangder.data.mapper.ChatRoomMapper
import com.viewpoint.dangder.data.remote.ChatRemoteDataSource
import com.viewpoint.dangder.domain.entity.ChatMessage
import com.viewpoint.dangder.domain.entity.ChatRoom
import com.viewpoint.dangder.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource
):ChatRepository  {
    override suspend fun joinChatRoom(myDogId: String, pairDogId: String): ChatRoom {
        val response = chatRemoteDataSource.joinChatRoom(myDogId= myDogId, pairDogId = pairDogId)

        if(response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val chatRoomData = response.data?.joinChatRoom ?: throw Exception("데이터가 존재하지 않습니다.")

        return ChatRoomMapper.mapToChatRoomEntity(chatRoomData)
    }

    override suspend fun fetchChatRoom(roomId: String): ChatRoom {
        val response = chatRemoteDataSource.fetchChatRoom(roomId)

        if(response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val chatRoomData = response.data?.fetchChatRoom ?:throw  Exception("데이터가 존재하지 않습니다.")

        return ChatRoomMapper.mapToChatRoomEntity(chatRoomData)
    }

    override suspend fun fetchChatMessagesByChatRoomId(roomId: String): List<ChatMessage> {
        val response = chatRemoteDataSource.fetchChatMessagesByChatRoomId(roomId)

        if(response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val messagesData = response.data?.fetchChatMessagesByChatRoomId ?:throw  Exception("데이터가 존재하지 않습니다.")

        return messagesData.map {
            ChatMessageMapper.mapToChatMessageEntity(it)
        }

    }

    override suspend fun fetchChatRooms(dogId: String): List<ChatRoom> {
        val response = chatRemoteDataSource.fetchChatRooms(dogId)

        if(response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val chatRooms = response.data?.fetchChatRooms ?: throw Exception("데이터가 존재하지 않습니다.")

        if(chatRooms.isEmpty()) throw Exception("데이터가 존재하지 않습니디.")

        return chatRooms
            .filter { it.id !=null && it.chatPairDog !=null }
            .map {
                ChatRoomMapper.mapToChatRoomEntity(it)
            }
    }
}