package com.viewpoint.dangder.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.entity.User
import com.viewpoint.dangder.usecase.CreateEmailTokenUseCase
import com.viewpoint.dangder.usecase.CreateUserUseCase
import com.viewpoint.dangder.usecase.VerifyEmailTokenUseCase
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
import org.mockito.kotlin.eq
import org.mockito.kotlin.given

class SignUpViewModelTest {

    private val mockCreateEmailTokenUseCase = Mockito.mock(CreateEmailTokenUseCase::class.java)
    private val mockVerifyEmailTokenUseCase = Mockito.mock(VerifyEmailTokenUseCase::class.java)
    private val mockCreateUserUseCase = Mockito.mock(CreateUserUseCase::class.java)
    private val mockSavedStateHandle = Mockito.mock(SavedStateHandle::class.java)

    private lateinit var signUpViewModel: SignUpViewModel
    private val mainThread = newSingleThreadContext(SignUpViewModel::class.java.simpleName)

    @BeforeEach
    fun setUp() {
        signUpViewModel =
            SignUpViewModel(
                createEmailTokenUseCase = mockCreateEmailTokenUseCase,
                verifyEmailTokenUseCase = mockVerifyEmailTokenUseCase,
                createUserUseCase = mockCreateUserUseCase,
                savedStateHandle = mockSavedStateHandle)
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("createEmailToken 메소드는")
    inner class DescribeOfCreateEmailToken {
        @Nested
        @DisplayName("입력이 올바른 이메일 값이면")
        inner class ContextWithCorrectEmail {
            @BeforeEach
            fun setUp() = runTest {
                given(mockCreateEmailTokenUseCase.invoke(any(), eq("signUp"))).willReturn(true)
            }

            @Test
            @DisplayName("다음 페이지 이동 액션을 발생한다.")
            fun `it publish GoToNextPage action`() = runTest {
                val email = "correct@tt.com"
                signUpViewModel.createEmailTokenForSignUp(email)
                val actual = signUpViewModel.action.test().awaitCount(3).values()
                assertThat(actual).contains(Actions.GoToNextPage)
            }
        }
    }

    @Nested
    @DisplayName("verifyEmailToken 메소드는")
    inner class DescribeOfVerifyEmailToken {
        @Nested
        @DisplayName("입력이 올바른 토큰 값이면")
        inner class ContextWithCorrectEmail {
            @BeforeEach
            fun setUp() = runTest {
                given(mockVerifyEmailTokenUseCase.invoke(eq("email"), eq("token"))).willReturn(true)
                given(mockSavedStateHandle.get<String>(signUpViewModel.EMAIL_KEY)).willReturn("email")
            }

            @Test
            @DisplayName("다음 페이지 이동 액션을 발생한다.")
            fun `it publish GoToNextPage action`() = runTest {

                signUpViewModel.verifyEmailToken("token")
                val actual = signUpViewModel.action.test().awaitCount(3).values()
                assertThat(actual).contains(Actions.GoToNextPage)
            }
        }
    }

    @Nested
    @DisplayName("createUser 메소드는")
    inner class DescribeOfCreateUser {
        @Nested
        @DisplayName("정상적으로 유저가 생성되면")
        inner class ContextWithCreateUserSuccess {
            private val mockUser = Mockito.mock(User::class.java)

            @BeforeEach
            fun setUp() = runTest {
                given(mockCreateUserUseCase.invoke(eq("email"), any())).willReturn(mockUser)
                given(mockSavedStateHandle.get<String>(signUpViewModel.EMAIL_KEY)).willReturn("email")
            }

            @Test
            @DisplayName("강아지 등록 페이지 이동 액션을 발행한다.")
            fun `it publish GoToInitDogPage action`() = runTest {

                signUpViewModel.createUser("password")
                val actual = signUpViewModel.action.test().awaitCount(3).values()
                assertThat(actual).contains(Actions.GoToInitDogPage(mockUser.id))
            }
        }
    }
}