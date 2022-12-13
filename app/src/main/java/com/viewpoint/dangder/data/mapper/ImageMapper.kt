package com.viewpoint.dangder.data.mapper

import com.viewpoint.FetchAroundDogsQuery
import com.viewpoint.FetchChatRoomsQuery
import com.viewpoint.FetchLoginUserQuery
import com.viewpoint.FetchOneDogQuery
import com.viewpoint.dangder.domain.entity.Image

object ImageMapper {
    fun mapToImageEntity(imageData : FetchLoginUserQuery.Img) : Image {
        return Image(
            url = imageData.img,
            isMain = imageData.isMain
        )
    }

    fun mapToImageEntity(imageData: FetchAroundDogsQuery.Img) = Image(
        url = imageData.img,
        isMain = imageData.isMain
    )

    fun mapToImageEntity(imageData : FetchOneDogQuery.Img) = Image(
        url = imageData.img,
        isMain = imageData.isMain
    )

    fun mapToImageEntity(imageData : FetchChatRoomsQuery.Img) = Image(
        url = imageData.img,
        isMain = imageData.isMain
    )
}