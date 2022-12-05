package com.viewpoint.dangder.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getAutoLoginSetting() : Flow<Boolean?>
    fun getUserAccount() : Flow<List<String>>
    fun getAccessToken() : Flow<String>
    suspend fun saveAccessToken(accessToken : String)
}