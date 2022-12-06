package com.viewpoint.dangder.data.mapper

import com.viewpoint.FetchDogsDistanceQuery
import com.viewpoint.FetchLoginUserQuery
import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.domain.entity.DogDistance

object DogDistanceMapper {

    fun mapToDogDistanceEntity(dogDistanceData: FetchDogsDistanceQuery.FetchDogsDistance) = DogDistance(
        dogId = dogDistanceData.dogId,
        distance = dogDistanceData.distance
    )

}