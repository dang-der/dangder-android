package com.viewpoint.dangder.repository

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.viewpoint.dangder.data.local.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource
): SettingsRepository {
    override fun getAutoLoginSetting(): Flow<Boolean?> {
        val key = booleanPreferencesKey(SettingsLocalDataSource.AUTO_LOGIN)
        return settingsLocalDataSource.getBoolean(key)
    }

    override fun getUserAccount(): Flow<List<String>> {
        val keyE = stringPreferencesKey(SettingsLocalDataSource.USER_ACCOUNT_E)
        val keyP = stringPreferencesKey(SettingsLocalDataSource.USER_ACCOUNT_P)

        return settingsLocalDataSource.getString(keyE).combine(settingsLocalDataSource.getString(keyP)) { e, t ->
            listOf(e, t)
        }
    }

    override fun getAccessToken(): Flow<String> {
        val key = stringPreferencesKey(SettingsLocalDataSource.TOKEN)
        return settingsLocalDataSource.getString(key)
    }

    override suspend fun saveAccessToken(accessToken: String) {
        val key = stringPreferencesKey(SettingsLocalDataSource.TOKEN)
        settingsLocalDataSource.putString(key, accessToken)
    }

}
