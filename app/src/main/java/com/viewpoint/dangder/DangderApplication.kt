package com.viewpoint.dangder

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.prefs.Preferences


@HiltAndroidApp
class DangderApplication : Application() {

    // At the top level of your kotlin file:

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}