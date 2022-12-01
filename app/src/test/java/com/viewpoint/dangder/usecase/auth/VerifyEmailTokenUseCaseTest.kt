package com.viewpoint.dangder.usecase.auth

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.repository.AuthRepository
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
import org.mockito.kotlin.eq
import org.mockito.kotlin.given

@DisplayName("VerifyEmailTokenUseCase (이메일 토큰 검사 유스케이스)는")
internal class VerifyEmailTokenUseCaseTest {
    @Mock
    private val mockAuthRepository = Mockito.mock(AuthRepository::class.java)
    private val verifyEmailTokenUseCase = VerifyEmailTokenUseCase(mockAuthRepository)
    private val mainThread = newSingleThreadContext(VerifyEmailTokenUseCase::class.java.simpleName)

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("올바른 인증코드를 입력하면")
    inner class ContextWithCorrectToken() {

        @BeforeEach
        fun setUp() = runTest {
            given(mockAuthRepository.verifyEmailToken(any(), eq("token"))).willReturn(true)
        }

        @Test
        @DisplayName("true를 리턴한다.")
        fun `it return true`() = runTest {

            val result = verifyEmailTokenUseCase("email","token")
            assertThat(result).isTrue()
        }
    }

    @Nested
    @DisplayName("올바르지 않은 인증코드를 입력하면")
    inner class ContextWithUnCorrectToken() {

        @BeforeEach
        fun setUp() = runTest {
            given(mockAuthRepository.verifyEmailToken(any(), any())).willReturn(false)
        }

        @Test
        @DisplayName("false를 리턴한다.")
        fun `it return false`() = runTest {
            val result = verifyEmailTokenUseCase("eamil", "unCorrectToken")
            assertThat(result).isFalse()
        }
    }


}