package com.viewpoint.dangder.usecase.dog

import com.viewpoint.dangder.repository.DogRepository
import com.viewpoint.type.CreateDogInput
import javax.inject.Inject

class CreateDogUseCase @Inject constructor(
    private val dogRepository: DogRepository
) {
    suspend operator fun invoke(createDogInput: CreateDogInput, dogRegNumber : String, ownerBirth : String): Boolean {
        try {
            val dog = dogRepository.createDog(createDogInput, dogRegNumber, ownerBirth)
            println(dog)
            return dog.id.isNotEmpty()
        }catch (e : Exception){
            throw e
        }
    }

}