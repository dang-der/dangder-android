package com.viewpoint.dangder.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class SettingsLocalDataSource @Inject constructor(
    private val settingsStore: DataStore<Preferences>
) {

    fun getBoolean(key: Preferences.Key<Boolean>): Flow<Boolean?> {
        return settingsStore.data.map {
            it[key] ?: false
        }
    }

    fun getString(key: Preferences.Key<String>): Flow<String> {
        return settingsStore.data.map {
            it[key] ?: ""
        }
    }

    fun getStringSet(key : Preferences.Key<Set<String>>): Flow<Set<String>> {
        return settingsStore.data.map {
            it[key] ?: setOf()
        }
    }

    suspend fun putString(key : Preferences.Key<String>, value: String){
        settingsStore.edit { settings ->
            settings[key] = value
        }
    }

    companion object {
        const val AUTO_LOGIN = "auto_login"
        const val TOKEN = "access_token"
        const val USER_ACCOUNT_E = "user_account_e"
        const val USER_ACCOUNT_P = "user_account_p"

    }

}