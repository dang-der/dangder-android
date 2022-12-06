package com.viewpoint.dangder.domain.repository

import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.domain.entity.DogDistance
import com.viewpoint.dangder.presenter.uimodel.InitDogInput

interface DogRepository {
    suspend fun getDogInfo(dogRegNum :String, ownerBirth :String): Boolean
    suspend fun fetchCharacters() : Array<String>
    suspend fun fetchInterests() : Array<String>
    suspend fun createDog(dogInput: InitDogInput, dogRegNum: String, ownerBirth: String): Dog
    suspend fun fetchAroundDogs(dogId : String, page : Double = 1.0) : List<Dog>
    suspend fun fetchDogsDistance(dogId: String) : List<DogDistance>
}