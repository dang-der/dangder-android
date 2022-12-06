package com.viewpoint.dangder.data.repository

import android.net.Uri
import com.apollographql.apollo3.api.Optional
import com.viewpoint.dangder.data.remote.DogRemoteDataSource
import com.viewpoint.dangder.domain.repository.DogRepository
import com.viewpoint.dangder.domain.repository.FileRepository
import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.domain.entity.DogDistance
import com.viewpoint.dangder.data.mapper.DogDistanceMapper
import com.viewpoint.dangder.data.mapper.DogMapper
import com.viewpoint.dangder.presenter.uimodel.InitDogInput
import com.viewpoint.type.CreateDogInput
import com.viewpoint.type.LocationInput
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(
    private val dogRemoteDataSource: DogRemoteDataSource,
    private val fileRepository: FileRepository,
) : DogRepository {
    override suspend fun getDogInfo(dogRegNum: String, ownerBirth: String): Boolean {
        val response = dogRemoteDataSource.getDogInfo(dogRegNum, ownerBirth)

        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        return response.data?.getDogInfo ?: throw Exception("강아지 등록 정보를 찾을 수 없습니다.")
    }

    override suspend fun fetchCharacters(): Array<String> {
        val response = dogRemoteDataSource.fetchCharacters()
        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val characters = response.data?.fetchCharacters?.map { it.character }
        return characters?.toTypedArray() ?: emptyArray()
    }

    override suspend fun fetchInterests(): Array<String> {
        val response = dogRemoteDataSource.fetchInterests()
        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val interests = response.data?.fetchInterestCategory?.map { it.interest }
        return interests?.toTypedArray() ?: emptyArray()
    }

    override suspend fun createDog(
        dogInput: InitDogInput,
        dogRegNum: String,
        ownerBirth: String
    ): Dog {
        val urls = fileUpload(dogInput.images)

        val createDogInput = CreateDogInput(
            age = dogInput.age,
            description = dogInput.description,
            img = Optional.present(urls),
            userId = dogInput.userId,
            interests = Optional.present(dogInput.interests?.toList()),
            characters = Optional.present(dogInput.characters?.toList()),
            locations = LocationInput(dogInput.lat, dogInput.lng)
            )

        val response = dogRemoteDataSource.createDog(createDogInput, dogRegNum, ownerBirth)
        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val dog = response.data?.createDog ?: throw Exception("강아지 등록에 실패했습니다.")

        return DogMapper.mapToDogEntity(dog)
    }

    override suspend fun fetchAroundDogs(dogId: String, page: Double): List<Dog> {
        val response = dogRemoteDataSource.fetchAroundDog(dogId, page)
        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val dogs = response.data?.fetchAroundDogs ?: throw Exception("데이터 불러오기에 실패했습니다.")

        return dogs.map { DogMapper.mapToDogEntity(it) }
    }

    override suspend fun fetchDogsDistance(dogId: String): List<DogDistance> {
        val response = dogRemoteDataSource.fetchDogsDistance(dogId)
        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val distances = response.data?.fetchDogsDistance ?: throw Exception("데이터 불러오기에 실패했습니다.")

        return distances.map { DogDistanceMapper.mapToDogDistanceEntity(it) }
    }

    private suspend fun fileUpload(images: Array<Uri>): List<String> {
        return fileRepository.uploadFiles(images)
    }
}