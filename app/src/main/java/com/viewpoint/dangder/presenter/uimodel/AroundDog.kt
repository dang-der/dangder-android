package com.viewpoint.dangder.presenter.uimodel

import com.viewpoint.dangder.domain.entity.Image

data class AroundDog(
    val id: String,
    val name: String? = null,
    val age : Int? = null,
    val description :String? = null,
    val gender : String?=null,
    val img: List<Image>? = emptyList(),
    val distance : Int?= null
){
    fun getMainImage(): String {
        img?: return ""
        if(img.isEmpty()) return ""
        return img.filter { it.isMain }.first().url
    }
}


