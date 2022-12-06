package com.viewpoint.dangder.domain.usecase.auth

import com.viewpoint.dangder.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyEmailTokenUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(email : String, token : String) : Boolean{
        try {
            return authRepository.verifyEmailToken(email, token)
        }catch (e : Exception){
            throw e
        }
    }

}
