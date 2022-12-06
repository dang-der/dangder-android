package com.viewpoint.dangder.domain.usecase.auth

import com.viewpoint.dangder.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email : String, password : String):Boolean {
        try{
            val token = authRepository.userLogin(email, password)
            authRepository.saveToken(token)
            return true
        }catch (e : Exception){
            throw e
        }

    }

}
