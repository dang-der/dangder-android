package com.viewpoint.dangder.mapper

import com.viewpoint.CreateDogMutation
import com.viewpoint.FetchAroundDogsQuery
import com.viewpoint.FetchLoginUserQuery
import com.viewpoint.dangder.entity.Dog
import com.viewpoint.dangder.entity.Image
import timber.log.Timber

object DogMapper {

    fun mapToDogEntity(dogData: FetchLoginUserQuery.Dog) = Dog(
        id = dogData.id,
        name = dogData.name,
        img = dogData.img.map { ImageMapper.mapToImage((it)) }
    )

    fun mapToDogEntity(dogData : CreateDogMutation.CreateDog) = Dog(
        id = dogData.id
    )

    fun mapToDogEntity(dogData: FetchAroundDogsQuery.FetchAroundDog): Dog {
        Timber.d(dogData.img.map { ImageMapper.mapToImage(it) }.toString())
        return Dog(
            id = dogData.id,
            name = dogData.name,
            img = dogData.img.map { ImageMapper.mapToImage(it) },
            age = dogData.age,
            description = dogData.description,
            gender = dogData.gender
        )
    }

}