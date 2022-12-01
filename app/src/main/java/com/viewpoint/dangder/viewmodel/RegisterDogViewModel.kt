package com.viewpoint.dangder.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.CheckRegisteredDogUseCase
import com.viewpoint.dangder.usecase.FetchCharactersUseCase
import com.viewpoint.dangder.usecase.FetchInterestsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.internal.aggregatedroot.codegen._com_viewpoint_dangder_DangderApplication
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterDogViewModel @Inject constructor(
    private val checkRegisteredDogUseCase: CheckRegisteredDogUseCase,
    private val fetchInterestsUseCase: FetchInterestsUseCase,
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    override val _action: PublishSubject<Actions> = PublishSubject.create()
    override val action: Observable<Actions>
        get() = _action

    private val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.d(exception.message)
        _action.onNext(Actions.ShowErrorMessage(exception.message ?: ""))
        _action.onNext(Actions.HideLoadingDialog)
    }

    var _registerNumber: String? = null
        get() =
            field ?: let {
                savedStateHandle.get<String>(REGISTER_NUMBER)
            }
        private set

    var _ownerBirth: String? = null
        get() = field ?: let {
            savedStateHandle.get<String>(OWNER_BIRTH)
        }
        private set

    var _images: Array<Uri>? = emptyArray()
        get() = field ?: let {
            savedStateHandle.get<Array<Uri>>(IMAGE_URIS)
        }
        private set

    var _age: Int? = null
        get() = field ?: let {
            savedStateHandle.get<Int>(AGE)
        }
        private set

    var _description: String? = null
        get() = field ?: let {
            savedStateHandle.get<String>(DESCRIPTION)
        }
        private set

    var _characters: Array<String>? = emptyArray()
        get() = field ?: let {
            savedStateHandle.get<Array<String>>(CHARACTERS)
        }
        private set

    var _interests: Array<String>? = emptyArray()
        get() = field ?: let {
            savedStateHandle.get<Array<String>>(INTERESTS)
        }
        private set


    fun checkRegisteredDog(dogRegNum: String, ownerBirth: String) = viewModelScope.launch(ceh) {
        _action.onNext(Actions.ShowLoadingDialog)

        val result = checkRegisteredDogUseCase(dogRegNum, ownerBirth)

        _action.onNext(Actions.HideLoadingDialog)
        if (result) {
            _registerNumber = dogRegNum
            _ownerBirth = ownerBirth

            _action.onNext(Actions.GoToNextPage)
        } else {
            _action.onNext(Actions.ShowErrorMessage("등록된 강아지 정보가 없습니다."))
        }
    }

    fun tempSave(images: List<Uri>, age: Int, description: String) {
        _images = images.toTypedArray()
        _age = age
        _description = description
        _action.onNext(Actions.GoToNextPage)
    }

    fun fetchCharacters() = viewModelScope.launch(ceh) {
        _action.onNext(Actions.ShowLoadingDialog)
        val characters = fetchCharactersUseCase()

        _action.onNext(Actions.HideLoadingDialog)
        _action.onNext(Actions.FetchCharacters(characters))
    }

    fun fetchInterests() = viewModelScope.launch(ceh) {
        _action.onNext(Actions.ShowLoadingDialog)
        val interests = fetchInterestsUseCase()

        _action.onNext(Actions.HideLoadingDialog)
        _action.onNext(Actions.FetchInterests(interests))
    }

    fun addCharacter(character: String) {
        _characters?.let {
            if (it.contains(character)) return

            _characters = it.toMutableList().apply {
                add(character)
            }.toTypedArray()
        }
        Timber.d(_characters?.toList().toString())
    }

    fun addInterest(interest: String) {
        _interests?.let {
            if (it.contains(interest)) return

            _interests = it.toMutableList().apply {
                add(interest)
            }.toTypedArray()
        }
        Timber.d(_interests?.toList().toString())
    }

    fun removeCharacter(character: String){
        _characters?.let {
            if(it.contains(character)){
                _characters = it.toMutableList().apply {
                    remove(character)
                }.toTypedArray()
            }
        }
    }

    fun removeInterest(interest: String){
        _interests?.let {
            if(it.contains(interest)){
                _interests = it.toMutableList().apply {
                    remove(interest)
                }.toTypedArray()
            }
        }
    }


    override fun onCleared() {
        savedStateHandle[REGISTER_NUMBER] = _registerNumber
        savedStateHandle[OWNER_BIRTH] = _ownerBirth
        savedStateHandle[IMAGE_URIS] = _images
        savedStateHandle[AGE] = _age
        savedStateHandle[DESCRIPTION] = _description
        savedStateHandle[CHARACTERS] = _characters
        savedStateHandle[INTERESTS] = _interests
        super.onCleared()
    }

    companion object {
        const val REGISTER_NUMBER = "registerNumber"
        const val OWNER_BIRTH = "ownerBirth"
        const val IMAGE_URIS = "images"
        const val AGE = "age"
        const val DESCRIPTION = "description"
        const val CHARACTERS = "character"
        const val INTERESTS = "interest"
    }
}