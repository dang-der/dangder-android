package com.viewpoint.dangder.mapper

import com.viewpoint.FetchLoginUserQuery
import com.viewpoint.dangder.entity.Dog

object DogMapper {

    fun mapToDogEntity(dogData: FetchLoginUserQuery.Dog) = Dog(
        id = dogData.id,
        name = dogData.name,
        img = dogData.img.map { ImageMapper.mapToImage((it)) }
    )

}