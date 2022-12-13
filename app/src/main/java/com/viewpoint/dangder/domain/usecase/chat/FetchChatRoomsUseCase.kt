package com.viewpoint.dangder.domain.usecase.chat

import com.viewpoint.dangder.domain.entity.ChatRoom
import com.viewpoint.dangder.domain.repository.ChatRepository
import javax.inject.Inject

class FetchChatRoomsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(dogId : String): List<ChatRoom> {
        try {
            return chatRepository.fetchChatRooms(dogId)
        }catch (e : Exception){
            throw e
        }
    }
}