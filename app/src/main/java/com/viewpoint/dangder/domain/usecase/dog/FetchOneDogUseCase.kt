package com.viewpoint.dangder.domain.usecase.dog

import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.domain.repository.DogRepository
import javax.inject.Inject

class FetchOneDogUseCase @Inject constructor(
    private val dogRepository: DogRepository
) {
    suspend operator fun invoke(dogId : String): Dog {
        try {
            return dogRepository.fetchOneDog(dogId)
        } catch (e: Exception) {
            throw e
        }
    }
}