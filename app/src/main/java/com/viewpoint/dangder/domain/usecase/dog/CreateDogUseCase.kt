package com.viewpoint.dangder.domain.usecase.dog

import com.viewpoint.dangder.domain.repository.DogRepository
import com.viewpoint.dangder.presenter.uimodel.InitDogInput
import javax.inject.Inject

class CreateDogUseCase @Inject constructor(
    private val dogRepository: DogRepository,
) {
    suspend operator fun invoke(
        dogInput : InitDogInput,
        dogRegNumber: String,
        ownerBirth: String
    ): Boolean {
        try {
            val dog = dogRepository.createDog(dogInput, dogRegNumber, ownerBirth)

            return dog.id.isNotEmpty()
        } catch (e: Exception) {
            throw e
        }
    }
}