package com.viewpoint.dangder.base

import androidx.lifecycle.ViewModel
import com.viewpoint.dangder.action.Actions
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    protected abstract val _action : PublishSubject<Actions>
    abstract val action : Observable<Actions>

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    protected fun showLoadingDialog(){
        _action.onNext(Actions.ShowLoadingDialog)
    }

    protected fun hideLoadingDialog(){
        _action.onNext(Actions.HideLoadingDialog)
    }
}