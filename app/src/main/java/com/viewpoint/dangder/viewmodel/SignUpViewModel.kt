package com.viewpoint.dangder.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.CheckLoggedInUseCase
import com.viewpoint.dangder.usecase.CreateEmailTokenUseCase
import com.viewpoint.dangder.usecase.LoginUseCase
import com.viewpoint.dangder.usecase.VerifyEmailTokenUseCase
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
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val EMAIL_KEY = "email"

    override val _action: PublishSubject<Actions> = PublishSubject.create()
    override val action: Observable<Actions>
        get() = _action

    var _email : String? = null
    private set

    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(exception.message)
        _action.onNext(Actions.ShowErrorMessage(exception.message ?: ""))
    }

    fun createEmailTokenForSignUp(email: String) = viewModelScope.launch(ceh) {
        val result = createEmailTokenUseCase(email, "signUp")
        if (result) {
            _email = email
            _action.onNext(Actions.GoToNextPage)
        }else {
            _action.onNext(
                Actions.ShowErrorMessage(
                    "이메일 전송에 실패했습니다."
                )
            )
        }
    }

    fun verifyEmailToken( token : String) = viewModelScope.launch(ceh){
        val email = _email?:savedStateHandle.get<String>(EMAIL_KEY) ?:return@launch
        val result = verifyEmailTokenUseCase(email, token)

        if(result) _action.onNext(Actions.GoToNextPage) else _action.onNext(Actions.ShowErrorMessage("인증코드가 일치하지 않습니다."))
    }

    override fun onCleared() {
        savedStateHandle[EMAIL_KEY] = _email
        super.onCleared()
    }

}

