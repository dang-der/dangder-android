package com.viewpoint.dangder.data.mapper

import com.viewpoint.FetchChatMessagesByChatRoomIdQuery
import com.viewpoint.FetchChatRoomsQuery
import com.viewpoint.dangder.domain.entity.*
import com.viewpoint.dangder.presenter.action.Actions

object ChatMessageMapper {
//    fun mapToChatMessage(messageData: FetchChatRoomQuery): ChatMessage {
//        return when (messageData.type) {
//            ChatMessageType.TEXT.name -> TextMessage(
//                id = messageData.id,
//                senderId = messageData.senderId,
//                type = ChatMessageType.TEXT,
//                message =  messageData.message
//            )
//            ChatMessageType.PLAN.name -> PlanMessage(
//                id = messageData.id,
//                senderId = messageData.senderId,
//                type = ChatMessageType.PLAN
//            )
//            ChatMessageType.PLACE.name -> PlaceMessage(
//                id = messageData.id,
//                senderId = messageData.senderId,
//                type = ChatMessageType.PLACE
//            )
//            else-> object : ChatMessage(){
//                override var id: String = ""
//                override var type: ChatMessageType = ChatMessageType.NONE
//                override var senderId: String=""
//            }
//        }
//    }

    fun mapToChatMessageEntity(messageData: FetchChatMessagesByChatRoomIdQuery.FetchChatMessagesByChatRoomId): ChatMessage {
        return createChatMessage(messageData.type, messageData.id, messageData.senderId, messageData.message)
    }

    fun mapToChatMessageEntity(messageData : FetchChatRoomsQuery.LastMessage): ChatMessage {
        return createChatMessage(messageData.type, messageData.id, messageData.senderId, messageData.message)
    }

    private fun createChatMessage(type: String, id: String,  senderId: String, message: String?) =
        when (type) {
            ChatMessageType.TEXT.name -> TextMessage(
                id = id,
                senderId = senderId,
                type = ChatMessageType.TEXT,
                message = message
            )
            ChatMessageType.PLAN.name -> PlanMessage(
                id = id,
                senderId = senderId,
                type = ChatMessageType.PLAN
            )
            ChatMessageType.PLACE.name -> PlaceMessage(
                id = id,
                senderId = senderId,
                type = ChatMessageType.PLACE
            )
            else -> object : ChatMessage() {
                override var id: String = ""
                override var type: ChatMessageType = ChatMessageType.NONE
                override var senderId: String = ""
            }
        }
}