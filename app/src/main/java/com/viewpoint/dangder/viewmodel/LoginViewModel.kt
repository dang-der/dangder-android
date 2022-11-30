package com.viewpoint.dangder.viewmodel

import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.CheckLoggedInUseCase
import com.viewpoint.dangder.usecase.FetchUserUseCase
import com.viewpoint.dangder.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkLoggedInUseCase: CheckLoggedInUseCase,
    private val loginUseCase: LoginUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
) : BaseViewModel() {


    override val _action: PublishSubject<Actions> = PublishSubject.create()
    override val action: Observable<Actions>
        get() = _action

    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(exception.message)
        _action.onNext(Actions.ShowErrorMessage(exception.message ?: ""))
        _action.onNext(Actions.HideLoadingDialog)
    }

    fun checkIsLogin() = viewModelScope.launch(ceh) {
        _action.onNext(Actions.ShowLoadingDialog)

        val result = checkLoggedInUseCase()


        if (result) {
            val isRegisterDog = checkRegisterDog()
            if(isRegisterDog) _action.onNext(Actions.GoToMainPage) else _action.onNext(Actions.GoToInitDogPage)
        } else {
            _action.onNext(Actions.GoToLoginPage)
        }

        _action.onNext(Actions.HideLoadingDialog)
    }


    fun login(email: String, pw: String) = viewModelScope.launch(ceh) {
        _action.onNext(Actions.ShowLoadingDialog)
        val result = loginUseCase(email, pw)

        if (result){
            val isRegistered = checkRegisterDog()
            if(isRegistered) _action.onNext(Actions.GoToMainPage) else _action.onNext(Actions.GoToInitDogPage)
        }

        _action.onNext(Actions.HideLoadingDialog)
    }

    private suspend fun checkRegisterDog(): Boolean {
        val user = fetchUserUseCase()
        return user.pet
    }


}

