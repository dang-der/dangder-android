package com.viewpoint.dangder.viewmodel

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.entity.User
import com.viewpoint.dangder.usecase.auth.CheckLoggedInUseCase
import com.viewpoint.dangder.usecase.auth.FetchUserUseCase
import com.viewpoint.dangder.usecase.auth.LoginUseCase
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

class LoginViewModelTest {

    private val mockCheckIsLoginUseCase = Mockito.mock(CheckLoggedInUseCase::class.java)
    private val mockLoginUseCase = Mockito.mock(LoginUseCase::class.java)
    private val mockFetchUserUseCase = Mockito.mock(FetchUserUseCase::class.java)

    private lateinit var loginViewModel: LoginViewModel
    private val mainThread = newSingleThreadContext(LoginViewModel::class.java.simpleName)

    @BeforeEach
    fun setUp() {
        loginViewModel =
            LoginViewModel(mockCheckIsLoginUseCase, mockLoginUseCase, mockFetchUserUseCase)
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("checkIsLogin 메소드는")
    inner class DescribeOfCheckIsLogin {

        @Nested
        @DisplayName("로그인한 상태라면")
        inner class ContextWithLogin {

            @BeforeEach
            fun setUp() = runTest {
                given(mockCheckIsLoginUseCase.invoke()).willReturn(true)
            }

            @Nested
            @DisplayName("강아지 등록이 되어 있는 사용자라면")
            inner class ContextWithRegisteredDog {
                @BeforeEach
                fun setUp() = runTest {
                    val mockUser = Mockito.mock(User::class.java)
                    given(mockUser.pet).willReturn(true)
                    given(mockFetchUserUseCase.invoke()).willReturn(mockUser)
                }

                @Test
                @DisplayName("메인 페이지 이동 액션을 발행한다.")
                fun `it publish GoToMainPage action`() = runTest {
                    loginViewModel.checkIsLogin()
                    val actual = loginViewModel.action.test().awaitCount(3).values()
                    assertThat(actual).contains(Actions.GoToMainPage)

                }
            }

            @Nested
            @DisplayName("강아지 등록이 되어 있는 않은 사용자라면")
            inner class ContextWithoutRegisteredDog {
                private val mockUser = Mockito.mock(User::class.java)

                @BeforeEach
                fun setUp() = runTest {
                    given((mockUser.id)).willReturn("testId")
                    given(mockUser.pet).willReturn(false)

                    given(mockFetchUserUseCase.invoke()).willReturn(mockUser)
                }

                @Test
                @DisplayName("강아지 등록 페이지 이동 액션을 발행한다.")
                fun `it publish GoToMainPage action`() = runTest {
                    loginViewModel.checkIsLogin()
                    val actual = loginViewModel.action.test().awaitCount(3).values()
                    assertThat(actual).contains(Actions.GoToInitDogPage(mockUser.id))
                }
            }
        }

        @Nested
        @DisplayName("로그인하지 않은 상태라면")
        inner class ContextWithoutLogin {

            @BeforeEach
            fun setUp() = runTest {
                given(mockCheckIsLoginUseCase.invoke()).willReturn(false)
            }

            @Test
            @DisplayName("로그인 페이지 이동 액션을 발행한다.")
            fun `it publish GotoLoginPage action`() = runTest {
                loginViewModel.checkIsLogin()
                val actual = loginViewModel.action.test().awaitCount(3).values()
                assertThat(actual).contains(Actions.GoToLoginPage)
            }
        }
    }

    @Nested
    @DisplayName("login 메소드는")
    inner class DescribeOfLogin {
        @Nested
        @DisplayName("로그인에 성공하면")
        inner class ContextWithSuccessLogin {


            @BeforeEach
            fun setUp() = runTest {

                given(mockLoginUseCase.invoke(any(), any())).willReturn(true)
            }

            @Nested
            @DisplayName("등록된 강아지가 있으면")
            inner class ContextWithInitializedDog {
                private val fakeUser = Mockito.mock(User::class.java)

                @BeforeEach
                fun setUp() = runTest {
                    given(fakeUser.id).willReturn("testId")
                    given(fakeUser.pet).willReturn(true)

                    given(mockFetchUserUseCase.invoke()).willReturn(fakeUser)
                }

                @Test
                @DisplayName("메인 페이지 이동 액션을 발행한다.")
                fun `it publish GoToMainPage action`() = runTest {
                    val email = "test@test.com"
                    val pw = "123qwe"
                    loginViewModel.login(email, pw)
                    val actual = loginViewModel.action.test().awaitCount(3).values()
                    assertThat(actual).contains(Actions.GoToMainPage)
                }
            }

            @Nested
            @DisplayName("등록된 강아지가 없으면")
            inner class ContextWithoutInitializedDog {

                private val fakeUser = Mockito.mock(User::class.java)

                @BeforeEach
                fun setUp() = runTest {
                    given(fakeUser.id).willReturn("testId")
                    given(fakeUser.pet).willReturn(false)

                    given(mockFetchUserUseCase.invoke()).willReturn(fakeUser)
                }

                @Test
                @DisplayName("강아지 등록 페이지 이동 액션을 발행한다.")
                fun `it publish GoToMainPage action`() = runTest {
                    val email = "test@test.com"
                    val pw = "123qwe"
                    loginViewModel.login(email, pw)
                    val actual = loginViewModel.action.test().awaitCount(3).values()
                    assertThat(actual).contains(Actions.GoToInitDogPage(fakeUser.id))
                }
            }
        }
    }
}