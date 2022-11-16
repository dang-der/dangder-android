package com.viewpoint.dangder.repository

import com.viewpoint.dangder.entity.User
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun userLogin(email : String, password : String): String
    suspend fun fetchLoginUser(): User
    suspend fun fetchAuthLoginSetting() : Flow<Boolean?>
    suspend fun fetchUserAccount() : Flow<List<String>>
    suspend fun saveToken(token : String)

}