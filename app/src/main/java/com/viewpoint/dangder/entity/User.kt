package com.viewpoint.dangder.entity

import com.viewpoint.FetchLoginUserQuery


data class User(
    val id : String,
    val email : String,
    val pet : Boolean,
    val isStop : Boolean,
    val dog : Dog? = null,
)




