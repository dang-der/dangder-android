package com.viewpoint.dangder.repository

import com.viewpoint.dangder.data.remote.DogRemoteDataSource
import com.viewpoint.dangder.entity.Dog
import com.viewpoint.dangder.mapper.DogMapper
import com.viewpoint.type.CreateDogInput
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(
    private val dogRemoteDataSource: DogRemoteDataSource
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
        createDogInput: CreateDogInput,
        dogRegNum: String,
        ownerBirth: String
    ): Dog {
        val response = dogRemoteDataSource.createDog(createDogInput, dogRegNum, ownerBirth)
        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val dog = response.data?.createDog ?:throw Exception("강아지 등록에 실패했습니다.")

        return DogMapper.mapToDogEntity(dog)
    }
}