package com.viewpoint.dangder.repository

interface DogRepository {
    suspend fun getDogInfo(dogRegNum :String, ownerBirth :String): Boolean
    suspend fun fetchCharacters() : Array<String>
    suspend fun fetchInterests() : Array<String>
}