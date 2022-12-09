package com.viewpoint.dangder.domain.usecase.chat

import com.viewpoint.dangder.domain.entity.ChatRoom
import com.viewpoint.dangder.domain.repository.ChatRepository
import javax.inject.Inject

class FetchChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(roomId : String): ChatRoom {
        try {
            return chatRepository.fetchChatRoom(roomId)
        }catch (e : Exception){
            throw e
        }
    }
}