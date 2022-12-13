package com.viewpoint.dangder.presenter.chat

import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.domain.entity.User
import com.viewpoint.dangder.domain.usecase.auth.FetchUserAndDogUseCase
import com.viewpoint.dangder.domain.usecase.chat.FetchChatRoomsUseCase
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.presenter.uimodel.ChatRoomItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val fetchUserAndDogUseCase: FetchUserAndDogUseCase,
    private val fetchChatRoomsUseCase: FetchChatRoomsUseCase
) : BaseViewModel() {
    override val _action: PublishSubject<Actions> = PublishSubject.create()
    override val action: Observable<Actions>
        get() = _action

    private val ceh = CoroutineExceptionHandler { coroutineContext, throwable ->
        Timber.e(throwable)
        hideLoadingDialog()
    }


    fun fetchChatRooms() = viewModelScope.launch(ceh) {
        showLoadingDialog()

        val dogId = fetchUserAndDog().dog?.id

        dogId ?: throw Exception("채팅방 목록을 찾을 수 없습니다.")

        val rooms = fetchChatRoomsUseCase(dogId)
        val roomItems = rooms
            .filter { it.pairDog != null && it.chatMessages?.isNotEmpty() == true }
            .map {
                ChatRoomItem(
                    id = it.id,
                    pairDogImage = it.pairDog!!.getMainImage(),
                    pairDogName = it.pairDog!!.name!!,
                    lastMessage = it.chatMessages!!.last()
                )
            }

        _action.onNext(Actions.FetchChatRooms(roomItems))

        hideLoadingDialog()
    }


    private suspend fun fetchUserAndDog(): User {
        return fetchUserAndDogUseCase()
    }

}