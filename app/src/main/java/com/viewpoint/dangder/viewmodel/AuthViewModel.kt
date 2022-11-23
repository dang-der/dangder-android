package com.viewpoint.dangder.viewmodel


import android.app.Notification.Action
import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.CheckLoggedInUseCase
import com.viewpoint.dangder.usecase.CreateEmailTokenUseCase
import com.viewpoint.dangder.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkLoggedInUseCase: CheckLoggedInUseCase,
    private val loginUseCase: LoginUseCase,
    private val createEmailTokenUseCase: CreateEmailTokenUseCase
) : BaseViewModel() {

    private val _action = BehaviorSubject.create<Actions>()
    val action: Observable<Actions> = _action

    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(exception.message)
        _action.onNext(Actions.ShowErrorMessage(exception.message ?: ""))
    }

    fun checkIsLogin() = viewModelScope.launch(ceh) {
        val result = checkLoggedInUseCase()
        if (result) _action.onNext(Actions.GoToMainPage) else _action.onNext(Actions.GoToLoginPage)
    }


    fun login(email: String, pw: String) = viewModelScope.launch(ceh) {
        val result = loginUseCase(email, pw)
        if (result) _action.onNext(Actions.GoToMainPage)
    }

    fun createEmailTokenForSignUp(email: String) = viewModelScope.launch(ceh) {
        val result = createEmailTokenUseCase(email, "signUp")
        if (result) _action.onNext(Actions.GoToNextPage) else _action.onNext(
            Actions.ShowErrorMessage(
                "이메일 전송에 실패했습니다."
            )
        )
    }

}