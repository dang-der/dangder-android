package com.viewpoint.dangder.data.mapper

import com.viewpoint.*
import com.viewpoint.dangder.domain.entity.Dog

object DogMapper {

    fun mapToDogEntity(dogData: FetchLoginUserQuery.Dog) = Dog(
        id = dogData.id,
        name = dogData.name,
        img = dogData.img.map { ImageMapper.mapToImageEntity((it)) }
    )

    fun mapToDogEntity(dogData : CreateDogMutation.CreateDog) = Dog(
        id = dogData.id
    )

    fun mapToDogEntity(dogData: FetchAroundDogsQuery.FetchAroundDog): Dog {
        return Dog(
            id = dogData.id,
            name = dogData.name,
            img = dogData.img.map { ImageMapper.mapToImageEntity(it) },
            age = dogData.age,
            description = dogData.description,
            gender = dogData.gender
        )
    }

    fun mapToDogEntity(dogDate : FetchOneDogQuery.FetchOneDog) : Dog{
        return Dog(
            id = dogDate.id,
            name = dogDate.name,
            age = dogDate.age,
            gender = dogDate.gender,
            isNeut = dogDate.isNeut,
            description = dogDate.description,
            interests = dogDate.interests.map { it.interest },
            characters = dogDate.characters.map { it.character },
            img = dogDate.img.map { ImageMapper.mapToImageEntity(it) }
        )
    }

    fun mapToDogEntity(dogData : FetchChatRoomQuery.Dog) = Dog(id = dogData.id)

}