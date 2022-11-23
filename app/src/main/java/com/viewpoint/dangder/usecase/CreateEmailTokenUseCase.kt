package com.viewpoint.dangder.usecase

import com.viewpoint.dangder.repository.AuthRepository
import javax.inject.Inject

class CreateEmailTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email : String) : Boolean{
        return authRepository.createEmailTokenForSignUp(email)
    }
}