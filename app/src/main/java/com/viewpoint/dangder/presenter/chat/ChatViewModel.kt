package com.viewpoint.dangder.presenter.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.domain.entity.ChatMessageType
import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.domain.usecase.chat.FetchChatMessagesUseCase
import com.viewpoint.dangder.domain.usecase.chat.FetchChatRoomUseCase
import com.viewpoint.dangder.domain.usecase.chat.FetchChatRoomsUseCase
import com.viewpoint.dangder.domain.usecase.dog.FetchOneDogUseCase
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.util.socket.OnData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val fetchChatRoomUseCase: FetchChatRoomUseCase,
    private val fetchChatMessagesUseCase: FetchChatMessagesUseCase,
    private val fetchOneDogUseCase: FetchOneDogUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    override val _action: PublishSubject<Actions> = PublishSubject.create()
    override val action: Observable<Actions>
        get() = _action

    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(exception.message)
        _action.onNext(Actions.ShowErrorMessage(exception.message ?: ""))
        _action.onNext(Actions.HideLoadingDialog)
    }

    var _chatRoomId: String? = null
        get() = field ?: let {
            savedStateHandle[CHAT_ROOM_ID]
        }
        private set
    var _pairDogId: String? = null
        get() = field ?: let {
            savedStateHandle[PAIR_DOG_ID]
        }
        private set

    var _pairDog: Dog? = null
        private set
    var _myDog: Dog? = null
        private set


    fun fetchChatRoomData(roomId: String) = viewModelScope.launch(ceh) {
        showLoadingDialog()
        val roomData = fetchChatRoomUseCase(roomId)

        _chatRoomId = roomData.id
        _pairDogId = roomData.pairDogId
        _myDog = roomData.dog

        val pairDog = fetchOneDogUseCase(roomData.pairDogId)
        _pairDog = pairDog

        if (roomId.isNotEmpty() && pairDog.id.isNotEmpty()) {
            _action.onNext(Actions.FetchChatRoomInfo(pairDog, roomData.dog, roomData.id))
        }
        hideLoadingDialog()
    }

    fun fetchMessages(roomId: String) = viewModelScope.launch(ceh) {
        val messages = fetchChatMessagesUseCase(roomId)
        if (messages.isNotEmpty()) {
            _action.onNext(Actions.FetchChatMessages(messages))
        }
    }

    fun receiveMessage(message: OnData) {
        val type = message.type
        val data = message.data
        val dog = message.dog

        dog ?: return

        when (type) {
            ChatMessageType.TEXT.name -> receiveTextMessage(data?.message, dog)
            ChatMessageType.PLAN.name -> receivePlanMessage(data?.meetAt, dog)
            ChatMessageType.PLACE.name -> receivePlaceMessage(data?.lat, data?.lng, dog)
        }
    }

    private fun receiveTextMessage(data: String?, dog: Dog) {
        data ?: return
        _action.onNext(Actions.ReceiveTextMessage(data, dog))
    }

    private fun receivePlaceMessage(lat: Double?, lng: Double?, dog: Dog) {
        lat ?: return
        lng ?: return
        _action.onNext(Actions.ReceivePlaceMessage(lat, lng, dog))
    }

    private fun receivePlanMessage(meetAt: String?, dog: Dog) {
        meetAt ?: return
        val format = "YYYYMMDD hh:mm a"
        // todo : 데이터 포맷 과정 추가
        _action.onNext(Actions.ReceivePlanMessage(meetAt, dog))
    }


    override fun onCleared() {
        savedStateHandle[CHAT_ROOM_ID] = _chatRoomId
        savedStateHandle[PAIR_DOG_ID] = _pairDogId
        super.onCleared()
    }

    companion object {
        const val CHAT_ROOM_ID = "chatRoomId"
        const val PAIR_DOG_ID = "pairDogId"
    }
}