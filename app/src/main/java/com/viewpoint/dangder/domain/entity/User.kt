package com.viewpoint.dangder.domain.entity


data class User(
    val id : String,
    val email : String,
    val pet : Boolean = false,
    val isStop : Boolean = false,
    val dog : Dog? = null,
)




