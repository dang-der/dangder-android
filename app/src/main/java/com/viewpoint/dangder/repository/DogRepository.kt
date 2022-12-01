package com.viewpoint.dangder.repository

import com.viewpoint.dangder.entity.Dog
import com.viewpoint.type.CreateDogInput

interface DogRepository {
    suspend fun getDogInfo(dogRegNum :String, ownerBirth :String): Boolean
    suspend fun fetchCharacters() : Array<String>
    suspend fun fetchInterests() : Array<String>
    suspend fun createDog(createDogInput: CreateDogInput, dogRegNum: String, ownerBirth: String):Dog
}