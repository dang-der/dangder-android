package com.viewpoint.dangder.repository

import com.viewpoint.dangder.data.remote.DogRemoteDataSource
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(
    private val dogRemoteDataSource: DogRemoteDataSource
) : DogRepository {
    override suspend fun getDogInfo(dogRegNum: String, ownerBirth: String): Boolean {
        val response = dogRemoteDataSource.getDogInfo(dogRegNum, ownerBirth)

        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        return response.data?.getDogInfo ?: throw Exception("강아지 등록 정보를 찾을 수 없습니다.")
    }
}