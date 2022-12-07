package com.viewpoint.dangder.domain.entity


data class Dog(
    val id: String,
    val name: String? = null,
    val age : Int? = null,
    val description :String? = null,
    val gender : String?=null,
    val img: List<Image>? = emptyList(),
    val isNeut : Boolean ? = null,
    val interests : List<String>? = emptyList(),
    val characters : List<String>? = emptyList(),
)
