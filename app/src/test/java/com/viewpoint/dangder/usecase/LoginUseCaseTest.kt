package com.viewpoint.dangder.usecase

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
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.given

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
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("올바른 이메일, 비밀번호를 입력했을 때")
    inner class CollectValue() {

        private val email = "test@test.com"
        private val password = "123qwe"

        @BeforeEach
        fun setUp()  = runTest {
            given(mockAuthRepository.userLogin(any(), any())).willReturn("testToken")
        }

        @Test
        @DisplayName("로그인에 성공할 수 있다.")
        fun `When the correct value is entered`() = runTest {
            assertThat(loginUseCase(any(), any())).isTrue()
        }
    }



}