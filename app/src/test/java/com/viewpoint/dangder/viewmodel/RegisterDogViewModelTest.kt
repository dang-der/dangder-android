package com.viewpoint.dangder.viewmodel

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.usecase.CheckRegisteredDogUseCase
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

class RegisterDogViewModelTest {
    private val mockCheckRegisteredDogUseCase = Mockito.mock(CheckRegisteredDogUseCase::class.java)

    private lateinit var registerDogViewModel: RegisterDogViewModel
    private val mainThread = newSingleThreadContext(RegisterDogViewModel::class.java.simpleName)

    @BeforeEach
    fun setUp() {
        registerDogViewModel =
            RegisterDogViewModel(
                checkRegisteredDogUseCase = mockCheckRegisteredDogUseCase
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
                registerDogViewModel.checkRegisteredDog("getNumber", "birth")
                val actual = registerDogViewModel.action.test().values()
                assertThat(actual).contains(Actions.GoToNextPage)
            }
        }
    }

}