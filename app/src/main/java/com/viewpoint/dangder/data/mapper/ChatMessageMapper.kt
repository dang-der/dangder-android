package com.viewpoint.dangder.data.mapper

import com.viewpoint.JoinChatRoomMutation
import com.viewpoint.dangder.domain.entity.*

object ChatMessageMapper {
    fun mapToChatMessage(messageData: JoinChatRoomMutation.ChatMessage): ChatMessage {
        return when (messageData.type) {
            ChatMessageType.TEXT.name -> TextMessage(
                id = messageData.id,
                senderId = messageData.senderId,
                type = ChatMessageType.TEXT,
                message =  messageData.message
            )
            ChatMessageType.PLAN.name -> PlanMessage(
                id = messageData.id,
                senderId = messageData.senderId,
                type = ChatMessageType.PLAN
            )
            ChatMessageType.PLACE.name -> PlaceMessage(
                id = messageData.id,
                senderId = messageData.senderId,
                type = ChatMessageType.PLACE
            )
            else-> object : ChatMessage(){
                override var id: String = ""
                override var type: ChatMessageType = ChatMessageType.NONE
                override var senderId: String=""
            }
        }
    }
}