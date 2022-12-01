package com.viewpoint.dangder.usecase

import com.viewpoint.dangder.repository.DogRepository
import javax.inject.Inject

class FetchCharactersUseCase @Inject constructor(
    private val dogRepository: DogRepository
) {
    suspend operator fun invoke(): Array<String> {
        try {
            return dogRepository.fetchCharacters()
        }catch (e : Exception){
            throw e
        }
    }
}