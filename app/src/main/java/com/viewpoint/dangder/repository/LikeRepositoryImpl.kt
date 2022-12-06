package com.viewpoint.dangder.repository

import com.viewpoint.dangder.data.remote.LikeRemoteDataSource
import com.viewpoint.type.CreateLikeInput
import com.viewpoint.type.CreateLikeOutput
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val likeRemoteDataSource: LikeRemoteDataSource
) : LikeRepository {
    override suspend fun createLike(sendDogId: String, receiveDogId: String): Boolean {
        val createLikeInput = CreateLikeInput(sendId =  sendDogId, receiveId = receiveDogId)

        val response = likeRemoteDataSource.createLike(createLikeInput = createLikeInput)

        if(response.hasErrors()) throw Exception(response.errors?.first()?.message)

        return (response.data?.createLike?.isMatch ?:throw Exception("데이터가 존재하지 않습니다."))
    }

    override suspend fun isLike(sendDogId: String, receiveDogId: String): Boolean {
        val response = likeRemoteDataSource.isLike(sendDogId, receiveDogId)
        if(response.hasErrors()) throw Exception(response.errors?.first()?.message)
        return response.data?.isLike ?: throw Exception("데이터가 존재하지 않습니다.")
    }
}