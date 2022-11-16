package com.viewpoint.dangder.usecase

import com.viewpoint.dangder.repository.AuthRepository
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
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
                return authLogin()
            }
            throw e
        }
    }

    private suspend fun authLogin(): Boolean {
        if(authRepository.fetchAuthLoginSetting().first()?.not() == true) return false



        val (email, password) =  authRepository.fetchUserAccount().first()

        return try {
            val token = authRepository.userLogin(email, password)
            authRepository.saveToken(token)
            return true
        }catch (e : Exception){
            throw e
        }
    }
}