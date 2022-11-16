package com.viewpoint.dangder.mapper

import com.viewpoint.FetchLoginUserQuery
import com.viewpoint.dangder.entity.Image

object ImageMapper {
    fun mapToImage(imageData : FetchLoginUserQuery.Img) : Image{
        return Image(
            url = imageData.img,
            isMain = imageData.isMain
        )
    }
}