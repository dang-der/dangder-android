package com.viewpoint.dangder.data.mapper

import com.viewpoint.FetchAroundDogsQuery
import com.viewpoint.FetchLoginUserQuery
import com.viewpoint.dangder.domain.entity.Image

object ImageMapper {
    fun mapToImage(imageData : FetchLoginUserQuery.Img) : Image {
        return Image(
            url = imageData.img,
            isMain = imageData.isMain
        )
    }

    fun mapToImage(imageData: FetchAroundDogsQuery.Img) = Image(
        url = imageData.img,
        isMain = imageData.isMain
    )
}