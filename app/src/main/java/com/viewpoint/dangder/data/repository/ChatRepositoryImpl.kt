package com.viewpoint.dangder.data.repository

import com.viewpoint.dangder.data.mapper.ChatRoomMapper
import com.viewpoint.dangder.data.remote.ChatRemoteDataSource
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
}