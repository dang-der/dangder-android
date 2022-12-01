package com.viewpoint.dangder.usecase

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.repository.AuthRepository
import com.viewpoint.dangder.usecase.auth.LoginUseCase
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

@DisplayName("로그인 유스케이스는")
internal class LoginUseCaseTest {
    @Mock
    private val mockAuthRepository = Mockito.mock(AuthRepository::class.java)
    private val loginUseCase = LoginUseCase(mockAuthRepository)
    private val mainThread = newSingleThreadContext(loginUseCase::class.java.simpleName)

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("가입된 계정을 입력하면")
    inner class ContextWithJoinedAccount() {

        @BeforeEach
        fun setUp() = runTest {
            given(mockAuthRepository.userLogin(any(), any())).willReturn("testToken")
        }

        @Test
        @DisplayName("로그인에 성공할 수 있다.")
        fun `When the correct value is entered`() = runTest {
            val result = loginUseCase(any(), any())
            assertThat(result).isTrue()
        }
    }

    @Nested
    @DisplayName("가입되지 않은 계정을 입력하면")
    inner class ContextWithNotJoinedAccount() {
        @BeforeEach
        fun setUp() = runTest {
            given(mockAuthRepository.userLogin(any(), any())).willAnswer { throw Exception("test") }
        }

        @Test
        @DisplayName("예외를 던진다")
        fun `When the correct value is entered`() = runTest {
            try {
                loginUseCase(any(), any())
            } catch (e: Exception) {
                assertThat(e.message).isEqualTo("test")
            }
        }
    }


}