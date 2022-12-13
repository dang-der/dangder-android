package com.viewpoint.dangder.domain.usecase.dog

import com.viewpoint.dangder.domain.repository.DogRepository
import com.viewpoint.dangder.domain.repository.SettingsRepository
import com.viewpoint.dangder.presenter.uimodel.AroundDogItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FetchAroundDogsUseCase @Inject constructor(
    private val dogRepository: DogRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(page : Double = 1.0): List<AroundDogItem> {
        try {
            val dogId = settingsRepository.getDogId().first()

            val aroundDogs = dogRepository.fetchAroundDogs(dogId, page)
            val distances = dogRepository.fetchDogsDistance(dogId)

            return aroundDogs.mapIndexed { index, dog ->
                val d = distances[index]

                return@mapIndexed AroundDogItem(
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