package com.viewpoint.dangder.domain.usecase.auth

import com.viewpoint.dangder.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * 로그인이 되어 있는지 확인하는 usecase
 */
class CheckLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return checkIsLogin()
    }

    private suspend fun checkIsLogin(): Boolean {
        try {
            authRepository.fetchLoginUser()
            return true
        } catch (e: Exception) {
            if (e.message.equals("Unauthorized")) {
                return autoLogin()
            }
            throw e
        }
    }

    private suspend fun autoLogin(): Boolean {
        if(authRepository.fetchAutoLoginSetting().first()?.not() == true) return false

        val (email, password) =  authRepository.fetchUserAccount().first()

        try {
            val token = authRepository.userLogin(email, password)
            authRepository.saveToken(token)
            return true
        }catch (e : Exception){
            throw e
        }
    }
}