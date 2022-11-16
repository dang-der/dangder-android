package com.viewpoint.dangder.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private const val serverUrl = "https://recipemaker.shop/graphql"

    @Provides
    fun provideApolloClient(okHttpClient: OkHttpClient) : ApolloClient = ApolloClient.builder()
        .serverUrl(serverUrl)
        .okHttpClient(okHttpClient)
        .build()

    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .build()
}
