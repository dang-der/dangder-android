package com.viewpoint.dangder.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getIsLoginSetting() : Flow<Boolean?>
    fun getUserAccountSetting() : Flow<List<String>>
    fun getAccessTokenSetting() : Flow<String>
    suspend fun saveAccessToken(accessToken : String)
}