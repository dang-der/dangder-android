package com.viewpoint.dangder.action



sealed class Actions {
    object GoToMainPage : Actions()
    object GoToLoginPage : Actions()
    object GoToNextPage : Actions()
    object GoToInitDogPage : Actions()
    object ShowLoadingDialog: Actions()
    object HideLoadingDialog: Actions()
    data class ShowErrorMessage (val message:String) : Actions()

}