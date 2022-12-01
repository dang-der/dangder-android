package com.viewpoint.dangder.usecase.dog

import com.viewpoint.dangder.repository.DogRepository
import javax.inject.Inject

class CheckRegisteredDogUseCase @Inject constructor(
    private val dogRepository: DogRepository
) {

    suspend operator fun invoke(dogRegNum : String, ownerBirth : String): Boolean {
        try {
            return dogRepository.getDogInfo(dogRegNum, ownerBirth)
        }catch (e : Exception){
            throw e
        }
    }
}