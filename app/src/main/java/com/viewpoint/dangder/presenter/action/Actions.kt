package com.viewpoint.dangder.presenter.action

import android.app.Notification.Action
import com.viewpoint.dangder.domain.entity.ChatMessage
import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.presenter.uimodel.AroundDog


sealed class Actions {
    // page move action
    object GoToMainPage : Actions()
    object GoToLoginPage : Actions()
    object GoToNextPage : Actions()
    data class GoToInitDogPage(val userId: String? = null) : Actions()
    data class GoToChatRoomPage(val roomId : String) : Actions()

    // common action
    object ShowLoadingDialog : Actions()
    object HideLoadingDialog : Actions()
    data class ShowErrorMessage(val message: String) : Actions()
    data class ShowSuccessMessage(val message: String) : Actions()
    data class ShowBuyPassTicketDialog(val pairDogId: String? = null) : Actions()

    // data action
    data class FetchCharacters(val data : Array<String>) : Actions()
    data class FetchInterests(val data: Array<String>) : Actions()

    data class Matched(val pairDog : Dog) : Actions()

    data class FetchAroundDogs(val data: List<AroundDog>) : Actions()
    data class FetchMoreAroundDogs(val data: List<AroundDog>) : Actions()

    data class FetchOneDog(val data : Dog) : Actions()

    data class FetchChatRoomInfo(val pairDog: Dog?, val myDog : Dog?, val roomId : String) : Actions()
    data class FetchChatMessages(val data : List<ChatMessage>) :Actions()

    // chat action
    data class ReceiveTextMessage(val data : String, val dog : Dog) : Actions()
    data class ReceivePlaceMessage(val lat : Double, val lng : Double, val dog : Dog) : Actions()
    data class ReceivePlanMessage(val meetAt : String, val dog : Dog) : Actions()


}