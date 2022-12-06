package com.viewpoint.dangder.domain.usecase.auth

import com.viewpoint.dangder.domain.entity.User
import com.viewpoint.dangder.domain.repository.AuthRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email : String, pw : String) : User {
        try {
            return authRepository.createUser(email, pw, false)
        }catch (e : Exception){
            throw e
        }
    }
}