package com.viewpoint.dangder.viewmodel

import android.location.Location
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseViewModel
import com.viewpoint.dangder.usecase.auth.FetchUserAndDogUseCase
import com.viewpoint.dangder.usecase.auth.FetchUserUseCase
import com.viewpoint.dangder.usecase.dog.CheckRegisteredDogUseCase
import com.viewpoint.dangder.usecase.dog.CreateDogUseCase
import com.viewpoint.dangder.usecase.dog.FetchCharactersUseCase
import com.viewpoint.dangder.usecase.dog.FetchInterestsUseCase
import com.viewpoint.dangder.view.data.InitDogInput
import com.viewpoint.type.CreateDogInput
import com.viewpoint.type.LocationInput
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InitDogViewModel @Inject constructor(
    private val checkRegisteredDogUseCase: CheckRegisteredDogUseCase,
    private val fetchInterestsUseCase: FetchInterestsUseCase,
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val createDogUseCase: CreateDogUseCase,
    private val fetchUserAndDogUseCase: FetchUserAndDogUseCase,
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

    var _userId: String? = null
        get() = field ?: let {
            savedStateHandle.get<String>(USER_ID)
        }


    fun createDog(location: Location) = viewModelScope.launch(ceh) {
        showLoading()

        if (_images == null) return@launch

        val createDogInput =
            _age?.let { age ->
                _description?.let { description ->
                    _userId?.let { userId ->
                        _images?.let { images ->
                            InitDogInput(
                                age = age,
                                description = description,
                                interests = _interests,
                                characters = _characters,
                                images = images,
                                userId = userId,
                                lat = location.latitude,
                                lng = location.longitude
                            )
                        }
                    }
                }
            }

        createDogInput ?: throw Exception("강아지 등록에 실패했습니다.")

        val createDogResult = createDogUseCase(
            dogInput = createDogInput,
            dogRegNumber = _registerNumber!!,
            ownerBirth = _ownerBirth!!
        )

        val fetchLoginAndDogResult = fetchUserAndDogUseCase()

        if (createDogResult && fetchLoginAndDogResult.id.isNotEmpty()) _action.onNext(Actions.GoToMainPage) else _action.onNext(Actions.GoToLoginPage)
        hideLoading()
    }

    fun checkRegisteredDog(dogRegNum: String, ownerBirth: String) = viewModelScope.launch(ceh) {
        showLoading()

        val result = checkRegisteredDogUseCase(dogRegNum, ownerBirth)

        hideLoading()

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
        showLoading()
        val characters = fetchCharactersUseCase()
        hideLoading()
        _action.onNext(Actions.FetchCharacters(characters))
    }

    fun fetchInterests() = viewModelScope.launch(ceh) {
        showLoading()
        val interests = fetchInterestsUseCase()

        hideLoading()
        _action.onNext(Actions.FetchInterests(interests))
    }

    fun addCharacter(character: String) {
        _characters?.let {
            if (it.contains(character)) return

            _characters = it.toMutableList().apply {
                add(character)
            }.toTypedArray()
        }
    }

    fun addInterest(interest: String) {
        _interests?.let {
            if (it.contains(interest)) return

            _interests = it.toMutableList().apply {
                add(interest)
            }.toTypedArray()
        }
    }

    fun removeCharacter(character: String) {
        _characters?.let {
            if (it.contains(character)) {
                _characters = it.toMutableList().apply {
                    remove(character)
                }.toTypedArray()
            }
        }
    }

    fun removeInterest(interest: String) {
        _interests?.let {
            if (it.contains(interest)) {
                _interests = it.toMutableList().apply {
                    remove(interest)
                }.toTypedArray()
            }
        }
    }

    private fun showLoading() {
        _action.onNext(Actions.ShowLoadingDialog)
    }

    private fun hideLoading() {
        _action.onNext(Actions.HideLoadingDialog)
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
        const val USER_ID = "userId"
    }
}