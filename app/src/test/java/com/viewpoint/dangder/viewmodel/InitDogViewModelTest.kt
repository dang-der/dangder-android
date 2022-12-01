package com.viewpoint.dangder.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.usecase.dog.CheckRegisteredDogUseCase
import com.viewpoint.dangder.usecase.dog.CreateDogUseCase
import com.viewpoint.dangder.usecase.dog.FetchCharactersUseCase
import com.viewpoint.dangder.usecase.dog.FetchInterestsUseCase
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
                savedStateHandle = mockSavedStateHandle
            )
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("checkRegisteredDog 메소드는")
    inner class DescribeOfCheckRegisteredDog{
        @Nested
        @DisplayName("입력한 정보가 동물보호관리시스템에 있으면")
        inner class ContextWithRegistered{
            @BeforeEach
            fun setUp() = runTest {
                given(mockCheckRegisteredDogUseCase.invoke(any(), any())).willReturn(true)
            }

            @Test
            @DisplayName("GoToNextPage 액션을 발행한다.")
            fun `it publish GoToNextPage`() = runTest {
                initDogViewModel.checkRegisteredDog("getNumber", "birth")
                val actual = initDogViewModel.action.test().values()
                assertThat(actual).contains(Actions.GoToNextPage)
            }
        }
    }

}