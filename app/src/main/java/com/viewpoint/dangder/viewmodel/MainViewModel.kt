package com.viewpoint.dangder.viewmodel

import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.auth.FetchUserAndDogUseCase
import com.viewpoint.dangder.usecase.dog.FetchAroundDogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchUserAndDogUseCase: FetchUserAndDogUseCase,
    private val fetchAroundDogsUseCase: FetchAroundDogsUseCase
) : BaseViewModel() {
    override val _action: PublishSubject<Actions> = PublishSubject.create()
    override val action: Observable<Actions>
        get() = _action

    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(exception.message)
        _action.onNext(Actions.ShowErrorMessage(exception.message ?: ""))
        _action.onNext(Actions.HideLoadingDialog)
    }

    fun fetchData() = viewModelScope.launch(ceh) {

        showLoadingDialog()

        val dogs = fetchAroundDogsUseCase()
        _action.onNext(Actions.FetchAroundDogs(dogs))

        hideLoadingDialog()

        Timber.tag("MainViewModel").d(dogs.toString())
    }



}