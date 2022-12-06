package com.viewpoint.dangder.presenter.login

import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.domain.entity.User
import com.viewpoint.dangder.domain.usecase.auth.FetchUserUseCase
import com.viewpoint.dangder.domain.usecase.auth.CheckLoggedInUseCase
import com.viewpoint.dangder.domain.usecase.auth.LoginUseCase
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
            val user = getLoginUser()
            if (user.pet) _action.onNext(Actions.GoToMainPage)
            else _action.onNext(Actions.GoToInitDogPage(userId = user.id))
        } else {
            _action.onNext(Actions.GoToLoginPage)
        }

        _action.onNext(Actions.HideLoadingDialog)
    }


    fun login(email: String, pw: String) = viewModelScope.launch(ceh) {
        _action.onNext(Actions.ShowLoadingDialog)
        val result = loginUseCase(email, pw)

        if (result) {
            val user = getLoginUser()

            if (user.pet)
                _action.onNext(Actions.GoToMainPage)
            else
                _action.onNext(
                Actions.GoToInitDogPage(
                    userId = user.id
                )
            )
        }

        _action.onNext(Actions.HideLoadingDialog)
    }

    private suspend fun getLoginUser(): User {
        return fetchUserUseCase()
    }


}

