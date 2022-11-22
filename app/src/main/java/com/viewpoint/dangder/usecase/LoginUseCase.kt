package com.viewpoint.dangder.usecase

import com.viewpoint.dangder.repository.AuthRepository
import timber.log.Timber
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
