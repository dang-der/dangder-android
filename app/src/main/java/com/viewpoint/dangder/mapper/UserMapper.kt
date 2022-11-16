package com.viewpoint.dangder.mapper

import com.viewpoint.FetchLoginUserQuery
import com.viewpoint.dangder.entity.User

object UserMapper {

    fun mapToUser(userData: FetchLoginUserQuery.User) = User(
        id = userData.id,
        email = userData.email,
        isStop = userData.isStop,
        pet = userData.pet
    )
}