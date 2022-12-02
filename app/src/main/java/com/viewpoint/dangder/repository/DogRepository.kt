package com.viewpoint.dangder.repository

import com.viewpoint.dangder.entity.Dog
import com.viewpoint.dangder.view.data.InitDogInput

interface DogRepository {
    suspend fun getDogInfo(dogRegNum :String, ownerBirth :String): Boolean
    suspend fun fetchCharacters() : Array<String>
    suspend fun fetchInterests() : Array<String>
    suspend fun createDog(dogInput: InitDogInput, dogRegNum: String, ownerBirth: String):Dog
}