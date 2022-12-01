package com.viewpoint.dangder.repository

import com.viewpoint.dangder.entity.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun userLogin(email : String, password : String): String
    suspend fun fetchLoginUser(): User
    suspend fun fetchSocialLoginUser() : User
    suspend fun fetchAutoLoginSetting() : Flow<Boolean?>
    suspend fun fetchUserAccount() : Flow<List<String>>
    suspend fun saveToken(token : String)
    suspend fun createEmailTokenForSignUp(email: String): Boolean
    suspend fun verifyEmailToken(email: String, token: String): Boolean
    suspend fun createUser(email: String,password: String, pet : Boolean): User
}