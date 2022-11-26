package com.viewpoint.dangder.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val createEmailTokenUseCase: CreateEmailTokenUseCase,
    private val verifyEmailTokenUseCase: VerifyEmailTokenUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val EMAIL_KEY = "email"
    val TOKEN_KEY = "token"

    override val _action: PublishSubject<Actions> = PublishSubject.create()
    override val action: Observable<Actions>
        get() = _action

    var _email: String? = null
        get() =
            field ?:let {
                savedStateHandle.get<String>(EMAIL_KEY)
            }
        private set

    var _token: String? = null
        get() = field?:let {
            savedStateHandle.get<String>(TOKEN_KEY)
        }
        private set

    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(exception.message)
        _action.onNext(Actions.ShowErrorMessage(exception.message ?: ""))
    }

    fun createEmailTokenForSignUp(email: String) = viewModelScope.launch(ceh) {
        showLoading()

        val result = createEmailTokenUseCase(email, "signUp")

        hideLoading()

        if (result) {
            _email = email
            _token = null
            _action.onNext(Actions.GoToNextPage)
        } else {
            _action.onNext(
                Actions.ShowErrorMessage(
                    "이메일 전송에 실패했습니다."
                )
            )
        }
    }

    fun verifyEmailToken(token: String) = viewModelScope.launch(ceh) {
        showLoading()

        val email = _email ?:return@launch
        val result = verifyEmailTokenUseCase(email, token)

        hideLoading()

        if (result) {
            _token = token
            _action.onNext(Actions.GoToNextPage)
        } else {
            _action.onNext(Actions.ShowErrorMessage("인증코드가 일치하지 않습니다."))
        }
    }

    fun createUser(password : String) = viewModelScope.launch(ceh){
        showLoading()

        val email = _email ?:return@launch
        val user = createUserUseCase(email, password)

        hideLoading()

        _action.onNext(Actions.GoToInitDogPage(user.id))
    }

    override fun onCleared() {
        savedStateHandle[EMAIL_KEY] = _email
        savedStateHandle[TOKEN_KEY] = _token
        super.onCleared()
    }

    private fun showLoading(){
        _action.onNext(Actions.ShowLoadingDialog)
    }

    private fun hideLoading(){
        _action.onNext(Actions.HideLoadingDialog)
    }
}

