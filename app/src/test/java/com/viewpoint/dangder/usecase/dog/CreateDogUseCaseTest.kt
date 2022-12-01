package com.viewpoint.dangder.usecase.dog

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.entity.Dog
import com.viewpoint.dangder.repository.DogRepository
import com.viewpoint.type.CreateDogInput
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

@DisplayName("CreateDogUseCase (내 강아지 등록) 유스케이스는는 ")
internal class CreateDogUseCaseTest {
    @Mock
    private val mockDogRepository = Mockito.mock(DogRepository::class.java)
    private val createDogUseCase = CreateDogUseCase(mockDogRepository)
    private val mainThread = newSingleThreadContext(CreateDogUseCase::class.java.simpleName)

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("내 강아지 등록에 성공한 경우")
    inner class ContextWithRegistered {
        private val mockCreateDogInput = Mockito.mock(CreateDogInput::class.java)
        private val mockDog = Mockito.mock(Dog::class.java)
        @BeforeEach
        fun setUp() = runTest {
            given(mockDogRepository.createDog(mockCreateDogInput, any(), any())).willReturn(mockDog)
        }

        @Test
        @DisplayName("true를 리턴한다")
        fun `it return true`() = runTest {
            val result = createDogUseCase.invoke(mockCreateDogInput, any(), any())
            assertThat(result).isTrue()
        }
    }
}