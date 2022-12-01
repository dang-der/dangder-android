package com.viewpoint.dangder.mapper

import com.viewpoint.CreateUserMutation
import com.viewpoint.FetchLoginUserQuery
import com.viewpoint.FetchSocialLoginUserQuery
import com.viewpoint.dangder.entity.User

object UserMapper {

    fun mapToUser(userData: FetchLoginUserQuery.User) = User(
        id = userData.id,
        email = userData.email,
        isStop = userData.isStop,
        pet = userData.pet
    )

    fun mapToUser(userData: CreateUserMutation.CreateUser) = User(
        id = userData.id,
        email = userData.email,
    )

    fun mapToUser(userData : FetchSocialLoginUserQuery.FetchSocialLoginUser) = User(
        id = userData.id,
        email = userData.email,
        isStop = userData.isStop,
        pet = userData.pet
    )
}