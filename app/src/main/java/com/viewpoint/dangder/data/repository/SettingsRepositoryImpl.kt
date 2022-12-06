package com.viewpoint.dangder.data.repository

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.viewpoint.dangder.data.local.SettingsLocalDataSource
import com.viewpoint.dangder.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource
): SettingsRepository {
    override suspend fun getAutoLoginSetting(): Flow<Boolean?> {
        val key = booleanPreferencesKey(SettingsLocalDataSource.AUTO_LOGIN)
        return settingsLocalDataSource.getBoolean(key)
    }

    override suspend fun getUserAccount(): Flow<List<String>> {
        val keyE = stringPreferencesKey(SettingsLocalDataSource.USER_ACCOUNT_E)
        val keyP = stringPreferencesKey(SettingsLocalDataSource.USER_ACCOUNT_P)

        return settingsLocalDataSource.getString(keyE).combine(settingsLocalDataSource.getString(keyP)) { e, t ->
            listOf(e, t)
        }
    }

    override suspend fun getAccessToken(): Flow<String> {
        val key = stringPreferencesKey(SettingsLocalDataSource.TOKEN)
        return settingsLocalDataSource.getString(key)
    }

    override suspend fun getDogId(): Flow<String> {
        val key = stringPreferencesKey(SettingsLocalDataSource.DOG_ID)
        return settingsLocalDataSource.getString(key)
    }

    override suspend fun saveAccessToken(accessToken: String) {
        val key = stringPreferencesKey(SettingsLocalDataSource.TOKEN)
        settingsLocalDataSource.putString(key, accessToken)
    }

    override suspend fun saveUserId(userId: String) {
        val key = stringPreferencesKey(SettingsLocalDataSource.USER_ID)
        settingsLocalDataSource.putString(key, userId)
    }

    override suspend fun saveDogId(dogId: String) {
        val key = stringPreferencesKey(SettingsLocalDataSource.DOG_ID)
        settingsLocalDataSource.putString(key, dogId)
    }

}
