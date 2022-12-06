package com.viewpoint.dangder.usecase

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.domain.entity.User
import com.viewpoint.dangder.domain.repository.AuthRepository
import com.viewpoint.dangder.domain.usecase.auth.FetchUserUseCase
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
import org.mockito.kotlin.given

@DisplayName("유저 정보 가져오기 유스케이스는")
internal class FetchUserUseCaseTest {
    @Mock
    private val mockAuthRepository = Mockito.mock(AuthRepository::class.java)
    private val fetchUserUseCase = FetchUserUseCase(mockAuthRepository)
    private val mainThread = newSingleThreadContext(fetchUserUseCase::class.java.simpleName)

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("사용자가 로그인 되어 있으면")
    inner class ContextWithLoggedInUser() {

        private val mockUser = Mockito.mock(User::class.java)

        @BeforeEach
        fun setUp() = runTest {
            given(mockAuthRepository.fetchSocialLoginUser()).willReturn(mockUser)
        }

        @Test
        @DisplayName("사용자 정보를 리턴한다.")
        fun `When the correct value is entered`() = runTest {
            val actual = fetchUserUseCase()
            assertThat(actual.id).isEqualTo(mockUser.id)
        }
    }

    @Nested
    @DisplayName("사용자가 로그인 되어 있지 않으면")
    inner class ContextWithNotLoggedInUser() {
        @BeforeEach
        fun setUp() = runTest {
            given(mockAuthRepository.fetchSocialLoginUser()).willAnswer { throw Exception("test") }
        }

        @Test
        @DisplayName("예외를 던진다")
        fun `When the correct value is entered`() = runTest {
            try {
                fetchUserUseCase()
            } catch (e: Exception) {
                assertThat(e.message).isEqualTo("test")
            }
        }
    }


}