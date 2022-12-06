package com.viewpoint.dangder.domain.usecase.auth

import com.viewpoint.dangder.domain.entity.User
import com.viewpoint.dangder.domain.repository.AuthRepository
import com.viewpoint.dangder.domain.repository.SettingsRepository
import javax.inject.Inject

class FetchUserAndDogUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): User {
        try {
            val user = authRepository.fetchLoginUser()
            val result = saveUserInfo(user)

            return if (result) user else throw Exception("데이터 저장에 실패했습니다.")
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun saveUserInfo(user: User): Boolean {
        user.dog?.let {
            settingsRepository.saveUserId(user.id)
            settingsRepository.saveDogId(user.dog.id)
            return true
        } ?: return false
    }
}