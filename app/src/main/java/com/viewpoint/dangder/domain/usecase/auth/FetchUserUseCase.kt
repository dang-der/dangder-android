package com.viewpoint.dangder.domain.usecase.auth

import com.viewpoint.dangder.domain.entity.User
import com.viewpoint.dangder.domain.repository.AuthRepository
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