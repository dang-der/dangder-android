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
import org.mockito.kotlin.*

@DisplayName("CreateDogUseCase (내 강아지 등록) 유스케이스는는 ")
internal class CreateDogUseCaseTest {
    @Mock
    private val mockDogRepository = Mockito.mock(DogRepository::class.java)
    private val createDogUseCase = CreateDogUseCase(mockDogRepository)
    private val mainThread = newSingleThreadContext(createDogUseCase::class.java.simpleName)

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    /**
     * given에서 그냥 any()사용시 willReturn에서 null을 반환하는 문제 해결
     * 1. any()는 null이 아닌 값을 반환하는데 왜 willReturn에서는 null이 나오는가? -> equqls를 정의 안해서 그럴 수 있음.
     * 2. Mockito.any(CreateDogInput::class.java) 사용시 must not be null 에러 발생 -> 일반 mockito에서 kotlin class는 final이기 때문에 mocking이 불가능하다고 한다.
     * 3. 아래 사이트를 보고 문제 해결
     * 참고 : https://withhamit.tistory.com/138
     * */

    @Nested
    @DisplayName("내 강아지 등록에 성공한 경우")
    inner class ContextWithRegistered {

        @BeforeEach
        fun setUp() = runTest {
            given(mockDogRepository.createDog(any(),any(), any())).willReturn(Dog(id="testId"))
        }

        @Test
        @DisplayName("true를 리턴한다")
        fun `it return true`() = runTest {
            val result = createDogUseCase.invoke(any(), any(), any())
            assertThat(result).isTrue()
        }
    }
}