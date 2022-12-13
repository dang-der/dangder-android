package com.viewpoint.dangder.presenter.chat

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseActivity
import com.viewpoint.dangder.databinding.ActivityChatRoomBinding
import com.viewpoint.dangder.domain.entity.ChatMessageType
import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.domain.entity.TextMessage
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.presenter.adapter.ChatMessageListAdapter
import com.viewpoint.dangder.util.showErrorSnackBar
import com.viewpoint.dangder.util.socket.OnData
import com.viewpoint.dangder.util.socket.SocketManager
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.socket.emitter.Emitter
import org.json.JSONObject
import timber.log.Timber
import java.util.UUID

@AndroidEntryPoint
class ChatRoomActivity : BaseActivity<ActivityChatRoomBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_chat_room

    private val chatViewModel: ChatViewModel by viewModels()
    private val chatMessageListAdapter by lazy { ChatMessageListAdapter() }
    private val socketManager = SocketManager(Emitter.Listener {
        val dataObj = Gson().fromJson(it.first().toString(), OnData::class.java)
        chatViewModel.receiveMessage(dataObj)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        socketManager.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }

    override fun initView() {
        handleClickClose()
        handleClickSend()
        initMessageList()
    }

    override fun initData() {
        val roomId = intent.getStringExtra("chatRoomId") ?: return

        chatViewModel.fetchChatRoomData(roomId)
        chatViewModel.fetchMessages(roomId)
    }

    override fun subscribe() {
        chatViewModel.action.subscribeBy(
            onNext = {
                when (it) {
                    is Actions.FetchChatRoomInfo -> {
                        it.pairDog ?: kotlin.run {
                            showErrorSnackBar(binding.root, "상대 강아지 정보를 불러오지 못했습니다.")
                            return@subscribeBy
                        }

                        it.myDog ?: kotlin.run {
                            showErrorSnackBar(binding.root, "내 강아지 정보를 불러오지 못했습니다.")
                            return@subscribeBy
                        }

                        initPairDogInfo(dog = it.pairDog)
                        socketManager.emitJoin(roomId = it.roomId, dog = it.myDog)
                    }
                    is Actions.FetchChatMessages -> {
                        chatMessageListAdapter.submitList(it.data)
                    }

                    is Actions.ReceiveTextMessage ->{
                        val textChatMessage = TextMessage(
                            id = UUID.randomUUID().toString(),
                            senderId = it.dog.id,
                            message = it.data
                        )

                        chatMessageListAdapter.addMessage(textChatMessage)
                    }
                    else -> {
                        if (it is Actions.ShowErrorMessage) {
                            showErrorSnackBar(binding.root, it.message)
                        }
                    }
                }
            },
            onError = {}
        ).addTo(compositeDisposable)
    }

    private fun handleClickClose() {
        binding.chatRoomBackBtn.setOnClickListener { finish() }
    }

    private fun initPairDogInfo(dog: Dog) {
        binding.pairDog = dog
    }

    private fun initMessageList() {
        binding.chatRoomMessageList.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        binding.chatRoomMessageList.adapter = chatMessageListAdapter
    }

    private fun handleClickSend(){
        binding.chatRoomSendBtn.setOnClickListener {
            val roomId = chatViewModel._chatRoomId
            val me = chatViewModel._myDog
            val contents = binding.chatRoomMessageInput.text.toString()

            if(contents.isBlank()) return@setOnClickListener
            roomId ?: return@setOnClickListener
            me ?: return@setOnClickListener

            socketManager.emitSend(
                roomId = roomId,
                type = ChatMessageType.TEXT,
                data = listOf(contents),
                dog = me
            )
        }
    }
}