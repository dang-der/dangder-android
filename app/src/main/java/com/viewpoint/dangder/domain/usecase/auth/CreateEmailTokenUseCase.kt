package com.viewpoint.dangder.domain.usecase.auth

import com.viewpoint.dangder.domain.repository.AuthRepository
import javax.inject.Inject

class CreateEmailTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email : String, type: String) : Boolean{
        try {
            return if(type == "signUp") authRepository.createEmailTokenForSignUp(email) else false
        }catch (e : Exception){
            throw e
        }
    }
}