package com.viewpoint.dangder.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.viewpoint.dangder.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SettingsLocalDataSource @Inject constructor(
    private val settingsStore: DataStore<Preferences>
) {

    suspend fun getBoolean(key: Preferences.Key<Boolean>): Flow<Boolean?> {
        return withContext(Dispatchers.IO){
            settingsStore.data.map {
                it[key] ?: false
            }
        }
    }

    suspend fun getString(key: Preferences.Key<String>): Flow<String> {
        return withContext(Dispatchers.IO){
            settingsStore.data.map {
                it[key] ?: ""
            }
        }
    }

    suspend fun putString(key : Preferences.Key<String>, value: String){
         withContext(Dispatchers.IO){
             settingsStore.edit { settings ->
                 settings[key] = value
             }
         }
    }


    companion object {
        const val AUTO_LOGIN = "autoLogin"
        const val TOKEN = "accessToken"
        const val USER_ACCOUNT_E = "userAccountE"
        const val USER_ACCOUNT_P = "userAccountP"
        const val USER_ID = "userId"
        const val DOG_ID = "dogId"
    }

}