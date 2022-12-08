package com.viewpoint.dangder.domain.usecase

import com.viewpoint.dangder.domain.entity.ChatRoom
import com.viewpoint.dangder.domain.repository.ChatRepository
import com.viewpoint.dangder.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EnterChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(pairDogId : String): ChatRoom {
        try {
            val myDogId = settingsRepository.getDogId().first()
            return chatRepository.joinChatRoom(myDogId = myDogId, pairDogId = pairDogId)
        } catch (e: Exception) {
            throw e
        }
    }
}