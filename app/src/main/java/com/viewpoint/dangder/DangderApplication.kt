package com.viewpoint.dangder

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.iamport.sdk.domain.core.Iamport
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.prefs.Preferences


@HiltAndroidApp
class DangderApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Iamport.create(this)
    }
}