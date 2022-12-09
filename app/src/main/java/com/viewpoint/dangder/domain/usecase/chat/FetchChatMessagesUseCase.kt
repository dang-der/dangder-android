package com.viewpoint.dangder.domain.usecase.chat

import com.viewpoint.dangder.domain.entity.ChatMessage
import com.viewpoint.dangder.domain.repository.ChatRepository
import javax.inject.Inject

class FetchChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(roomId : String): List<ChatMessage> {
        try {
            return chatRepository.fetchChatMessagesByChatRoomId(roomId)
        }catch (e : Exception){
            throw e
        }
    }
}