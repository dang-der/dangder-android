package com.viewpoint.dangder.usecase.dog

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.repository.DogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.given

@DisplayName("CheckRegisteredDogUseCase (반려견 등록 여부 확인) 유스케이스는는 ")
internal class CheckRegisteredDogUseCaseTest {
    @Mock
    private val mockDogRepository = Mockito.mock(DogRepository::class.java)
    private val checkRegisteredDogUseCase = CheckRegisteredDogUseCase(mockDogRepository)
    private val mainThread = newSingleThreadContext(CheckRegisteredDogUseCase::class.java.simpleName)

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("강아지가 동물보호관리시스템에 등록되어 있는 경우")
    inner class ContextWithRegistered {
        @BeforeEach
        fun setUp() = runTest {
            given(mockDogRepository.getDogInfo(any(), any())).willReturn(true)
        }

        @Test
        @DisplayName("true 값을 리턴한다.")
        fun `it return true`() = runTest {

            val result = checkRegisteredDogUseCase.invoke("", "")
            assertThat(result).isTrue()
        }
    }

    @Nested
    @DisplayName("강아지가 동물보호관리시스템에 등록되어 있지 않은 경우 ")
    inner class ContextWithoutRegistered {
        @BeforeEach
        fun setUp() = runTest{
            given(mockDogRepository.getDogInfo(any(), any())).willReturn(false)
        }

        @Test
        @DisplayName("false 값을 리턴한다.")
        fun `it return false`() = runTest {
            val result = checkRegisteredDogUseCase("", "")
            assertThat(result).isFalse()
        }

    }
}