package com.viewpoint.dangder.usecase.auth

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.domain.entity.User
import com.viewpoint.dangder.domain.repository.AuthRepository
import com.viewpoint.dangder.domain.repository.SettingsRepository
import com.viewpoint.dangder.domain.usecase.auth.FetchUserAndDogUseCase
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

@DisplayName("사용자의 정보와 등록된 강아지를 가져오는 유스케이스는")
internal class FetchUserAndDogUseCaseTest {
    @Mock
    private val mockAuthRepository = Mockito.mock(AuthRepository::class.java)
    private val mockSettingsRepository = Mockito.mock(SettingsRepository::class.java)

    private val fetchUserAndDogUseCase = FetchUserAndDogUseCase(mockAuthRepository, mockSettingsRepository)

    private val mainThread = newSingleThreadContext(fetchUserAndDogUseCase::class.java.simpleName)

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("로그인한 사용자가 정보를 요청하면")
    inner class ContextWithJoinedAccount() {

        private val fakeUser = Mockito.mock(User::class.java)
        private val fakeDog = Mockito.mock(Dog::class.java)

        private val testUserId = "testUserId"
        private val testDogId = "testDogId"
        @BeforeEach
        fun setUp() = runTest {
            given(fakeUser.dog).willReturn(fakeDog)
            given(fakeUser.id).willReturn(testUserId)
            given(fakeDog.id).willReturn(testDogId)

            given(mockAuthRepository.fetchLoginUser()).willReturn(fakeUser)
        }

        @Test
        @DisplayName("사용자 정보와 사용자가 등록한 강아지 정보를 받아 저장한다.")
        fun `it return user info`() = runTest {
            val actual = fetchUserAndDogUseCase.invoke()

            assertThat(actual.id).isEqualTo(testUserId)
            assertThat(actual.dog?.id).isEqualTo(testDogId)
        }
    }

    @Nested
    @DisplayName("로그인하지 않은 사용자가 정보를 요청하면")
    inner class ContextWithoutJoinedAccount() {

        @BeforeEach
        fun setUp() = runTest {
            given(mockAuthRepository.fetchLoginUser()).willAnswer{throw Exception("서버에서 어케 오더라...")}
        }

        @Test
        @DisplayName("예외를 던진다.")
        fun `it throw exception`() = runTest {
            try {
                fetchUserAndDogUseCase.invoke()
            }catch (e : Exception){
                assertThat(e.message).isEqualTo("서버에서 어케 오더라...")
            }
        }
    }



}