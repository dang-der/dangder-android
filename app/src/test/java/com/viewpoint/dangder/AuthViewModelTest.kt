package com.viewpoint.dangder

import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.usecase.CheckLoggedInUseCase
import com.viewpoint.dangder.usecase.LoginUseCase
import com.viewpoint.dangder.viewmodel.AuthViewModel
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

class AuthViewModelTest {


    private val mockCheckIsLoginUseCase = Mockito.mock(CheckLoggedInUseCase::class.java)
    private val mockLoginUseCase = Mockito.mock(LoginUseCase::class.java)
    private lateinit var authViewModel: AuthViewModel
    private val mainThread = newSingleThreadContext(AuthViewModel::class.java.simpleName)

    @BeforeEach
    fun setUp() {
        authViewModel = AuthViewModel(mockCheckIsLoginUseCase, mockLoginUseCase)
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("checkIsLogin 메소드는")
    inner class DescribeOfCheckIsLogin{

        @Nested
        @DisplayName("로그인한 상태라면")
        inner class ContextWithLogin{

            @BeforeEach
            fun setUp() = runTest {
                given(mockCheckIsLoginUseCase.invoke()).willReturn(true)
            }

            @Test
            @DisplayName("메인 페이지 이동 액션을 발행한다.")
            fun `it publish GoToMainPage action`() = runTest {
                authViewModel.checkIsLogin()
                authViewModel.action.test().awaitCount(1).assertValue(Actions.GoToMainPage)
            }
        }

        @Nested
        @DisplayName("로그인하지 않은 상태라면")
        inner class ContextWithoutLogin{

            @BeforeEach
            fun setUp() = runTest {
                given(mockCheckIsLoginUseCase.invoke()).willReturn(false)
            }

            @Test
            @DisplayName("로그인 페이지 이동 액션을 발행한다.")
            fun `it publish GotoLoginPage action`() = runTest {
                authViewModel.checkIsLogin()
                authViewModel.action.test().awaitCount(1).assertValue(Actions.GoToLoginPage)
            }
        }
    }

    @Nested
    @DisplayName("login 메소드는")
    inner class DescribeOfLogin{
        @Nested
        @DisplayName("올바른 이메일, 패스워드 값이라면")
        inner class ContextWithCollectValue{
            @BeforeEach
            fun setUp() = runTest {
                given(mockLoginUseCase.invoke(any(), any())).willReturn(true)
            }

            @Test
            @DisplayName("메인 페이지 이동 액션을 발행한다.")
            fun `it publish GoToMainPage action`() = runTest {
                val email = "test@test.com"
                val pw = "123qwe"
                authViewModel.login(email, pw)
                authViewModel.action.test().awaitCount(1).assertValue(Actions.GoToMainPage)
            }
        }
    }



}