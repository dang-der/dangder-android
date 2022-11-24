package com.viewpoint.dangder.viewmodel

import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.CheckLoggedInUseCase
import com.viewpoint.dangder.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkLoggedInUseCase: CheckLoggedInUseCase,
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {


    override val _action: BehaviorSubject<Actions> = BehaviorSubject.create()
    override val action: Observable<Actions>
        get() = _action

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

}

