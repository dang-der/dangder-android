package com.viewpoint.dangder.viewmodel

import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.CheckRegisteredDogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterDogViewModel @Inject constructor(
    private val checkRegisteredDogUseCase: CheckRegisteredDogUseCase
) : BaseViewModel() {


    override val _action: PublishSubject<Actions> = PublishSubject.create()
    override val action: Observable<Actions>
        get() = _action

    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(exception.message)
        _action.onNext(Actions.ShowErrorMessage(exception.message ?: ""))
        _action.onNext(Actions.HideLoadingDialog)
    }


    fun checkRegisteredDog(dogRegNum: String, ownerBirth: String) = viewModelScope.launch(ceh) {
        _action.onNext(Actions.ShowLoadingDialog)

        val result = checkRegisteredDogUseCase(dogRegNum, ownerBirth)

        _action.onNext(Actions.HideLoadingDialog)
        if(result) {
            _action.onNext(Actions.GoToNextPage)
        }else{
            _action.onNext(Actions.ShowErrorMessage("등록된 강아지 정보가 없습니다."))
        }
    }
}