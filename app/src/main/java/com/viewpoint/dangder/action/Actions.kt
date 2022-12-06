package com.viewpoint.dangder.action

import com.viewpoint.dangder.view.data.AroundDog


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

    // data action
    data class FetchCharacters(val data : Array<String>) : Actions()
    data class FetchInterests(val data: Array<String>) : Actions()
    data class FetchAroundDogs(val data: List<AroundDog>) : Actions()
}