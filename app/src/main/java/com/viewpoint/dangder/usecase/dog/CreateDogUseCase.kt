package com.viewpoint.dangder.usecase.dog

import android.net.Uri
import com.apollographql.apollo3.api.Optional
import com.viewpoint.dangder.repository.DogRepository
import com.viewpoint.dangder.repository.FileRepository
import com.viewpoint.type.CreateDogInput
import com.viewpoint.type.LocationInput
import javax.inject.Inject

class CreateDogUseCase @Inject constructor(
    private val dogRepository: DogRepository,
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(
        images: Array<Uri>,
        age: Int,
        description: String,
        characters: Array<String>? = emptyArray(),
        interests: Array<String>? = emptyArray(),
        userId : String,
        lat : Double,
        lng : Double,
        dogRegNumber: String,
        ownerBirth: String
    ): Boolean {
        try {
            val urls = fileRepository.uploadFiles(images)
            val createDogInput = CreateDogInput(
                age = age,
                description = description,
                interests = Optional.present(interests?.toList()),
                characters = Optional.present(characters?.toList()),
                img = Optional.present(urls),
                userId = userId,
                locations = LocationInput(lat, lng),
            )
            val dog = dogRepository.createDog(createDogInput, dogRegNumber, ownerBirth)
            return dog.id.isNotEmpty()
        } catch (e: Exception) {
            throw e
        }
    }

}