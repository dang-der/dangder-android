package com.viewpoint.dangder.domain.entity

enum class ChatMessageType(name: String) {
    TEXT(name = "text"),
    PLACE(name = "place"),
    PLAN(name = "plan"),
    NONE(name = "none")
}

abstract class ChatMessage {
    abstract var id: String
    abstract var type: ChatMessageType
    abstract var senderId: String
}

data class TextMessage(
    override var id: String,
    override var senderId: String,
    override var type: ChatMessageType = ChatMessageType.TEXT,
    val message : String? = null
) : ChatMessage()

data class PlaceMessage(
    override var id: String,
    override var type: ChatMessageType = ChatMessageType.PLACE,
    override var senderId: String,
    val lat : Double? = null,
    val lng : Double? = null
) : ChatMessage()

data class PlanMessage(
    override var id: String,
    override var type: ChatMessageType = ChatMessageType.PLAN,
    override var senderId: String,
    val meetAt : String? = null
) : ChatMessage()
