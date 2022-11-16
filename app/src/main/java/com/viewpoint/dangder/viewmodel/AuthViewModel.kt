package com.viewpoint.dangder.viewmodel


import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.CheckLoggedInUseCase
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
) : BaseViewModel() {

    private val _isLogin = BehaviorSubject.create<Boolean>()
    val isLogin: Observable<Boolean> = _isLogin

    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(AuthViewModel::class.simpleName, exception)
    }

    fun checkIsLogin() {
        viewModelScope.launch(ceh) {
            val result = checkLoggedInUseCase()
            _isLogin.onNext(result)
        }
    }

}