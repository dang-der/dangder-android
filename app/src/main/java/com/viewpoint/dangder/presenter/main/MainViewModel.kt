package com.viewpoint.dangder.presenter.main

import androidx.lifecycle.viewModelScope
import com.iamport.sdk.data.sdk.IamPortRequest
import com.iamport.sdk.data.sdk.PayMethod
import com.iamport.sdk.domain.core.Iamport
import com.viewpoint.dangder.BuildConfig
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.domain.entity.User
import com.viewpoint.dangder.domain.usecase.EnterChatRoomUseCase
import com.viewpoint.dangder.domain.usecase.auth.FetchUserUseCase
import com.viewpoint.dangder.domain.usecase.dog.FetchAroundDogsUseCase
import com.viewpoint.dangder.domain.usecase.dog.FetchOneDogUseCase
import com.viewpoint.dangder.domain.usecase.like.CreateLikeUseCase
import com.viewpoint.dangder.domain.usecase.payment.BuyPassTicketUseCase
import com.viewpoint.dangder.domain.usecase.payment.CheckUserBuyPassTicketUseCase
import com.viewpoint.dangder.presenter.action.Actions
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchAroundDogsUseCase: FetchAroundDogsUseCase,
    private val createLikeUseCase: CreateLikeUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val buyPassTicketUseCase: BuyPassTicketUseCase,
    private val checkUserBuyPassTicketUseCase: CheckUserBuyPassTicketUseCase,
    private val fetchOneDogUseCase: FetchOneDogUseCase,
    private val enterChatRoomUseCase: EnterChatRoomUseCase
) : BaseViewModel() {
    override val _action: PublishSubject<Actions> = PublishSubject.create()
    override val action: Observable<Actions>
        get() = _action

    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(exception.message)
        _action.onNext(Actions.ShowErrorMessage(exception.message ?: ""))
        _action.onNext(Actions.HideLoadingDialog)
    }


    fun fetchAroundDogs() = viewModelScope.launch(ceh) {
        showLoadingDialog()

        val dogs = fetchAroundDogsUseCase()
        _action.onNext(Actions.FetchAroundDogs(dogs))

        hideLoadingDialog()
    }

    fun fetchOneDog(dogId: String) = viewModelScope.launch(ceh) {
        showLoadingDialog()

        val dog = fetchOneDogUseCase(dogId)

        _action.onNext(Actions.FetchOneDog(dog))

        hideLoadingDialog()
    }

    fun like(receiveDogId: String) = viewModelScope.launch(ceh) {
        val isMatched = createLikeUseCase(receiveDogId = receiveDogId)

        if (isMatched){
            showLoadingDialog()
            val pairDog = fetchOneDogUseCase(receiveDogId)
            hideLoadingDialog()
            _action.onNext(Actions.Matched(pairDog))

        }
    }


    // pairDogId ??? null??? ?????? ??? : ???????????? ???????????? ?????? ??????????????? ???????????? ??????
    fun requestBuyPassTicket(iamport: Iamport, pairDogId: String? = null) = viewModelScope.launch(ceh) {
        showLoadingDialog()

        val request = createPaymentRequest()

        iamport.payment(
            iamPortRequest = request,
            userCode = BuildConfig.IAMPORT_KEY,
            paymentResultCallback = {

                if (it?.imp_success?.not() == true) {
                    _action.onNext(Actions.ShowErrorMessage("????????? ??????????????????."))
                    return@payment
                }

                viewModelScope.launch(ceh) {
                    it?.imp_uid?.let { impUid ->
                        val result = buyPassTicketUseCase(impUid, 100.0)
                        if (result) {
                            _action.onNext(Actions.ShowSuccessMessage("?????? ?????? ?????? ??????!"))

                            enterChatRoom(pairDogId)

                        } else _action.onNext(Actions.ShowErrorMessage("????????? ??????????????????."))
                    }
                }.start()
            }
        )
        hideLoadingDialog()
    }

    fun checkBuyPassTicket(pairDogId : String) = viewModelScope.launch(ceh) {
        showLoadingDialog()

        val isPurchase = checkUserBuyPassTicketUseCase()

        if (!isPurchase) {
            hideLoadingDialog()
            _action.onNext(Actions.ShowBuyPassTicketDialog(pairDogId))
            return@launch
        }

        enterChatRoom(pairDogId)
    }

    fun fetchMore(page: Int) = viewModelScope.launch(ceh) {
        showLoadingDialog()
        val moreData = fetchAroundDogsUseCase(page.toDouble())
        _action.onNext(Actions.FetchMoreAroundDogs(moreData))
        hideLoadingDialog()
    }

    fun enterChatRoom(pairDogId: String? = null) = viewModelScope.launch(ceh){
        showLoadingDialog()
        pairDogId ?: return@launch

        val chatRoom = enterChatRoomUseCase(pairDogId=pairDogId)
        _action.onNext(Actions.GoToChatRoomPage(roomId = chatRoom.id))
        hideLoadingDialog()
    }

    private suspend fun createPaymentRequest(): IamPortRequest {
        val userInfo = fetchUserInfo()
        val amount = "100"

        return IamPortRequest(
            pg = "nice",
            pay_method = PayMethod.card.name,
            name = "???????????? ??????",
            amount = amount,
            buyer_email = userInfo.email,
            merchant_uid = "pass_${System.currentTimeMillis()}"
        )
    }

    private suspend fun fetchUserInfo(): User {
        return fetchUserUseCase()
    }


}