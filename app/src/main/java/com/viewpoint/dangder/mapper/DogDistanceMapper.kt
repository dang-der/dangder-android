package com.viewpoint.dangder.mapper

import com.viewpoint.FetchDogsDistanceQuery
import com.viewpoint.FetchLoginUserQuery
import com.viewpoint.dangder.entity.Dog
import com.viewpoint.dangder.entity.DogDistance

object DogDistanceMapper {

    fun mapToDogDistanceEntity(dogDistanceData: FetchDogsDistanceQuery.FetchDogsDistance) = DogDistance(
        dogId = dogDistanceData.dogId,
        distance = dogDistanceData.distance
    )

}