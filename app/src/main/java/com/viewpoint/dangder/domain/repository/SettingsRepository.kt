package com.viewpoint.dangder.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getAutoLoginSetting() : Flow<Boolean?>
    suspend fun getUserAccount() : Flow<List<String>>
    suspend fun getAccessToken() : Flow<String>
    suspend fun getDogId() : Flow<String>
    suspend fun saveAccessToken(accessToken : String)
    suspend fun saveUserId(userId : String)
    suspend fun saveDogId(dogId : String)
}