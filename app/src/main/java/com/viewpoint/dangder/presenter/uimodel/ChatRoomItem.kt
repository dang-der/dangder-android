package com.viewpoint.dangder.presenter.uimodel

import com.viewpoint.dangder.domain.entity.ChatMessage
import com.viewpoint.dangder.domain.entity.ChatMessageType
import com.viewpoint.dangder.domain.entity.TextMessage

data class ChatRoomItem(
    val id: String,
    val pairDogName: String,
    val pairDogImage: String,
    val lastMessage: ChatMessage
) {
    fun lastMessageToString(): String {
        return when (lastMessage.type) {
            ChatMessageType.TEXT -> (lastMessage as? TextMessage)?.message ?: ""
            ChatMessageType.PLAN -> "$pairDogName 님이 약속을 공유했습니다."
            ChatMessageType.PLACE -> "$pairDogName 님이 장소를 공유했습니다."
            else -> ""
        }
    }
}
