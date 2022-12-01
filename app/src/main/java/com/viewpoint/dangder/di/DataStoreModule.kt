package com.viewpoint.dangder.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.apollographql.apollo3.ApolloClient
import com.viewpoint.dangder.data.remote.FileRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

val Context.settingsStore by preferencesDataStore(name = "settings")

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    @Provides
    fun providesDataStore(@ApplicationContext context: Context) : DataStore<Preferences> = context.settingsStore

    @Provides
    fun proviedsFileRemoteDataSource(apolloClient: ApolloClient, @ApplicationContext context: Context) = FileRemoteDataSource(apolloClient, context)
}


