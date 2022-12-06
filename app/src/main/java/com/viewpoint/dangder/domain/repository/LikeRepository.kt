package com.viewpoint.dangder.domain.repository

import com.viewpoint.type.CreateLikeOutput

interface LikeRepository {
    suspend fun createLike(sendDogId : String, receiveDogId : String): Boolean
    suspend fun isLike(sendDogId: String,receiveDogId: String) : Boolean
}