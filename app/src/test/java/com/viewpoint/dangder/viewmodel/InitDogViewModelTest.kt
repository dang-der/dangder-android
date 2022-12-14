package com.viewpoint.dangder.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.domain.usecase.auth.FetchUserAndDogUseCase
import com.viewpoint.dangder.domain.usecase.dog.CheckRegisteredDogUseCase
import com.viewpoint.dangder.domain.usecase.dog.CreateDogUseCase
import com.viewpoint.dangder.domain.usecase.dog.FetchCharactersUseCase
import com.viewpoint.dangder.domain.usecase.dog.FetchInterestsUseCase
import com.viewpoint.dangder.presenter.initdog.InitDogViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.given

class InitDogViewModelTest {
    private val mockCheckRegisteredDogUseCase = Mockito.mock(CheckRegisteredDogUseCase::class.java)
    private val mockFetchCharacterUseCase = Mockito.mock(FetchCharactersUseCase::class.java)
    private val mockFetchInterestsUseCase = Mockito.mock(FetchInterestsUseCase::class.java)
    private val mockCreateDogUseCase = Mockito.mock(CreateDogUseCase::class.java)
    private val mockFetchUserAndDogUseCase = Mockito.mock(FetchUserAndDogUseCase::class.java)

    private val mockSavedStateHandle = Mockito.mock(SavedStateHandle::class.java)

    private lateinit var initDogViewModel: InitDogViewModel
    private val mainThread = newSingleThreadContext(InitDogViewModel::class.java.simpleName)

    @BeforeEach
    fun setUp() {
        initDogViewModel =
            InitDogViewModel(
                checkRegisteredDogUseCase = mockCheckRegisteredDogUseCase,
                fetchCharactersUseCase = mockFetchCharacterUseCase,
                fetchInterestsUseCase = mockFetchInterestsUseCase,
                createDogUseCase = mockCreateDogUseCase,
                fetchUserAndDogUseCase = mockFetchUserAndDogUseCase,
                savedStateHandle = mockSavedStateHandle
            )
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("checkRegisteredDog ????????????")
    inner class DescribeOfCheckRegisteredDog{
        @Nested
        @DisplayName("????????? ????????? ?????????????????????????????? ?????????")
        inner class ContextWithRegistered{
            @BeforeEach
            fun setUp() = runTest {
                given(mockCheckRegisteredDogUseCase.invoke(any(), any())).willReturn(true)
            }

            @Test
            @DisplayName("GoToNextPage ????????? ????????????.")
            fun `it publish GoToNextPage`() = runTest {
                initDogViewModel.checkRegisteredDog("getNumber", "birth")
                val actual = initDogViewModel.action.test().values()
                assertThat(actual).contains(Actions.GoToNextPage)
            }
        }
    }

}