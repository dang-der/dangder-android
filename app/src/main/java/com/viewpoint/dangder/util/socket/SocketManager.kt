package com.viewpoint.dangder.util.socket

import com.google.gson.Gson
import com.viewpoint.dangder.domain.entity.ChatMessageType
import com.viewpoint.dangder.domain.entity.Dog
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import timber.log.Timber
import java.net.URISyntaxException


class SocketManager(
    private val messageOnListener: Emitter.Listener
) {
    private val socket: Socket

    init {
        try {
            socket = IO.socket(SERVER_URL)
        } catch (e: URISyntaxException) {
            throw e
        }
    }

    fun emitSend(roomId: String, type: ChatMessageType, dog: Dog, data: List<String>) {
        val content = when (type) {
            ChatMessageType.TEXT -> ContentsData(message = data[0])
            ChatMessageType.PLACE -> ContentsData(
                lat = data[0].toDouble(),
                lng = data[1].toDouble()
            )
            ChatMessageType.PLAN -> ContentsData(meetAt = data[0])
            else -> ContentsData()
        }
        val emitData = EmitData(roomId = roomId, type = type.name, dog = dog, data = content)
        val jsonObj = JSONObject(Gson().toJson(emitData))
        socket.emit(EMIT_SEND_EVENT, jsonObj)
    }

    fun connect() {
        onMessage()
        socket.connect()
        socket.onAnyIncoming {
            Timber.d(it.toList().toString())
        }
    }

    fun disconnect() {
        socket.disconnect()
        offMessage()
    }


    fun emitJoin(roomId: String, dog: Dog) {
        val data = EmitData(roomId = roomId, dog = dog)
        val jsonObj = JSONObject(Gson().toJson(data))
        socket.emit(EMIT_JOIN_EVENT, jsonObj)
    }

    private fun onMessage() {
        socket.on(ON_MESSAGE_EVENT, messageOnListener)
    }

    private fun offMessage() {
        socket.off(ON_MESSAGE_EVENT, messageOnListener)
    }

    private companion object {
        const val ON_MESSAGE_EVENT = "message"
        const val EMIT_SEND_EVENT = "send"
        const val EMIT_JOIN_EVENT = "join"
        const val SERVER_URL = "https://recipemaker.shop/dangderchats"
    }


}