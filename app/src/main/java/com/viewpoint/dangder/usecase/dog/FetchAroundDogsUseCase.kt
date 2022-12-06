package com.viewpoint.dangder.usecase.dog

import com.viewpoint.dangder.repository.DogRepository
import com.viewpoint.dangder.repository.SettingsRepository
import com.viewpoint.dangder.view.data.AroundDog
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class FetchAroundDogsUseCase @Inject constructor(
    private val dogRepository: DogRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): List<AroundDog> {
        try {
            val dogId = settingsRepository.getDogId().first()

            val aroundDogs = dogRepository.fetchAroundDogs(dogId)
            val distances = dogRepository.fetchDogsDistance(dogId)

            return aroundDogs.mapIndexed { index, dog ->
                val d = distances[index]
                return@mapIndexed AroundDog(
                    id = dog.id,
                    name = dog.name,
                    description = dog.description,
                    age = dog.age,
                    gender = dog.gender,
                    img = dog.img,
                    distance = d.distance
                )
            }

        }catch (e : Exception){
            throw e
        }
    }
}