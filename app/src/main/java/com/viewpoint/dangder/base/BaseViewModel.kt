package com.viewpoint.dangder.base

import androidx.lifecycle.ViewModel
import com.viewpoint.dangder.action.Actions
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    protected abstract val _action : BehaviorSubject<Actions>
    abstract val action : Observable<Actions>

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}