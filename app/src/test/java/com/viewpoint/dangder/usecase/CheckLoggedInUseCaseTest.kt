package com.viewpoint.dangder.usecase

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.entity.User
import com.viewpoint.dangder.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

@DisplayName("CheckLoggedInUseCase (로그인 여부 확인) 유스케이스는는 ")
internal class CheckLoggedInUseCaseTest {
    @Mock
    private val mockAuthRepository = Mockito.mock(AuthRepository::class.java)
    private val checkLoggedInUseCase = CheckLoggedInUseCase(mockAuthRepository)
    private val mainThread = newSingleThreadContext(CheckLoggedInUseCase::class.java.simpleName)

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("로그인이 되어 있으면")
    inner class ContextWithLogged {
        @BeforeEach
        fun setUp() = runTest {
            val mockUser = Mockito.mock(User::class.java)
            given(mockAuthRepository.fetchLoginUser()).willReturn(mockUser)
        }

        @Test
        @DisplayName("true값을 리턴한다.")
        fun `it return true`() = runTest {
            val result = checkLoggedInUseCase()
            assertThat(result).isTrue()
        }
    }

    @Nested
    @DisplayName("로그인이 되어 있지 않으면")
    inner class ContextWithoutLogged {
        @BeforeEach
        fun setUp() = runTest{
            given(mockAuthRepository.fetchLoginUser()).willAnswer { throw Exception("Unauthorized") }
        }

        @Nested
        @DisplayName("자동 로그인 여부를 확인한다.")
        inner class ContextWithAutoLogin{

            @Test
            @DisplayName("자동 로그인 설정이 되어 있으면 true를 리턴한다.")
            fun `it return true`() = runTest {
                given(mockAuthRepository.fetchAutoLoginSetting()).willReturn(flow { emit(true) })
                given(mockAuthRepository.fetchUserAccount()).willReturn(flow { emit(listOf("email", "pw")) })
                given(mockAuthRepository.userLogin(any(), any())).willReturn("token")

                val result = checkLoggedInUseCase()

                assertThat(result).isTrue()
            }

            @Test
            @DisplayName("자동 로그인이 설정되어 있으면 false를 리턴한다.")
            fun `throw exception`() = runTest{
                given(mockAuthRepository.fetchAutoLoginSetting()).willReturn(flow { emit(false) })
                given(mockAuthRepository.fetchUserAccount()).willReturn(flow { emit(listOf("email", "pw")) })
                given(mockAuthRepository.userLogin(any(), any())).willReturn("token")

                val result = checkLoggedInUseCase()

                assertThat(result).isFalse()
            }


        }

    }
}