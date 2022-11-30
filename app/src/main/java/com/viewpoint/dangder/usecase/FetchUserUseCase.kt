package com.viewpoint.dangder.usecase

import com.viewpoint.dangder.entity.User
import com.viewpoint.dangder.repository.AuthRepository
import javax.inject.Inject

class FetchUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): User {
        try {
            return authRepository.fetchSocialLoginUser()
        }catch (e : Exception){
            throw e
        }
    }
}