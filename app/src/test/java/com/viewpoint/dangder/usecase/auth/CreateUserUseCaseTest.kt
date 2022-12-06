package com.viewpoint.dangder.usecase.auth

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.domain.entity.User
import com.viewpoint.dangder.domain.repository.AuthRepository
import com.viewpoint.dangder.domain.usecase.auth.CreateUserUseCase
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

@DisplayName("CreateUserUseCase (유저 생성 유스케이스)는")
internal class CreateUserUseCaseTest {
    @Mock
    private val mockAuthRepository = Mockito.mock(AuthRepository::class.java)
    private val createUserUseCase = CreateUserUseCase(mockAuthRepository)
    private val mainThread = newSingleThreadContext(CreateUserUseCase::class.java.simpleName)

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("계정을 생성하면")
    inner class ContextWithCorrectToken() {

        val mockUser = Mockito.mock(User::class.java)

        @BeforeEach
        fun setUp() = runTest {
            given(mockAuthRepository.createUser(any(), any(), any())).willReturn(mockUser)
        }

        @Test
        @DisplayName("유저 정보를 리턴한다.")
        fun `it return true`() = runTest {

            val result = createUserUseCase("email", "password")
            assertThat(result).isEqualTo(mockUser)
        }
    }

}