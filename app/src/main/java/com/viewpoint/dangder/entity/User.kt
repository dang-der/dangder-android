package com.viewpoint.dangder.entity


data class User(
    val id : String,
    val email : String,
    val pet : Boolean = false,
    val isStop : Boolean = false,
    val dog : Dog? = null,
)




