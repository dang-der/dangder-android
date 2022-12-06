package com.viewpoint.dangder.viewmodel

import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.auth.FetchUserAndDogUseCase
import com.viewpoint.dangder.usecase.dog.FetchAroundDogsUseCase
import com.viewpoint.dangder.usecase.like.CreateLikeUseCase
import com.viewpoint.dangder.view.data.AroundDog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchAroundDogsUseCase: FetchAroundDogsUseCase,
    private val createLikeUseCase: CreateLikeUseCase,
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
    }

    fun like(receiveDogId : String) = viewModelScope.launch(ceh){
        val isMatched = createLikeUseCase(receiveDogId = receiveDogId)
        if(isMatched) _action.onNext(Actions.Matched(receiveDogId))
    }




}