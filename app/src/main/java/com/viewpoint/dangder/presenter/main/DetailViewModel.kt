package com.viewpoint.dangder.presenter.main

import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.domain.usecase.dog.FetchOneDogUseCase
import com.viewpoint.dangder.presenter.action.Actions
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val fetchOneDogUseCase: FetchOneDogUseCase
)  : BaseViewModel() {
    override val _action: PublishSubject<Actions> = PublishSubject.create()
    override val action: Observable<Actions>
        get() = _action


    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(exception.message)
        _action.onNext(Actions.ShowErrorMessage(exception.message ?: ""))
        _action.onNext(Actions.HideLoadingDialog)
    }

    fun fetchData(dogId : String) = viewModelScope.launch(ceh) {
        showLoadingDialog()

        val dog = fetchOneDogUseCase(dogId)

        _action.onNext(Actions.FetchOneDog(dog))

        hideLoadingDialog()
    }
}