package com.viewpoint.dangder.domain.usecase.dog

import com.viewpoint.dangder.domain.repository.DogRepository
import javax.inject.Inject

class FetchInterestsUseCase @Inject constructor(
    private val dogRepository: DogRepository
) {
    suspend operator fun invoke(): Array<String> {
        try {
            return dogRepository.fetchInterests()
        }catch (e : Exception){
            throw e
        }
    }
}