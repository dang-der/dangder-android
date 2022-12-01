package com.viewpoint.dangder.entity

data class Dog(
    val id: String,
    val name: String? = null,
    val img: List<Image>? = emptyList()
)
