package com.viewpoint.dangder.presenter.action

import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.presenter.uimodel.AroundDog


sealed class Actions {
    // page move action
    object GoToMainPage : Actions()
    object GoToLoginPage : Actions()
    object GoToNextPage : Actions()
    data class GoToInitDogPage(val userId: String? = null) : Actions()

    // common action
    object ShowLoadingDialog : Actions()
    object HideLoadingDialog : Actions()
    data class ShowErrorMessage(val message: String) : Actions()
    data class ShowSuccessMessage(val message: String) : Actions()

    // data action
    data class FetchCharacters(val data : Array<String>) : Actions()
    data class FetchInterests(val data: Array<String>) : Actions()

    data class Matched(val pairDog : Dog) : Actions()

    data class FetchAroundDogs(val data: List<AroundDog>) : Actions()
    data class FetchMoreAroundDogs(val data: List<AroundDog>) : Actions()

    data class FetchOneDog(val data : Dog) : Actions()

    //
    object ShowBuyPassTicketDialog : Actions()

}