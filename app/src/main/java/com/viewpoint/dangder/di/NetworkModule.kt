package com.viewpoint.dangder.di

import androidx.datastore.preferences.core.stringPreferencesKey
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.viewpoint.dangder.data.local.SettingsLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.net.CookieManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private const val serverUrl = "https://recipemaker.shop/graphql"

    @Singleton
    @Provides
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient = ApolloClient.builder()
        .serverUrl(serverUrl)
        .okHttpClient(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(settingsLocalDataSource: SettingsLocalDataSource): OkHttpClient {

        val headerLogging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.tag("OkHttp - Header").d(it)
        }).apply {
            setLevel(HttpLoggingInterceptor.Level.HEADERS)
            redactHeader("Authorization")
            redactHeader("Cookie")
        }

        val bodyLogging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.tag("OkHttp - Body").d(it)
        }).apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

        return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(settingsLocalDataSource))
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .addInterceptor(headerLogging)
            .addInterceptor(bodyLogging)
            .build()
    }

}

class AuthorizationInterceptor(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val key = stringPreferencesKey(SettingsLocalDataSource.TOKEN)
        val token = runBlocking {
            val t = settingsLocalDataSource.getString(key).first()
            Timber.tag("interceptor").d("get t : ${t}")
            t
        }

        Timber.tag("interceptor").d("get Token : $token")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        Timber.d(request.header("Authorization"))
        return chain.proceed(request)
    }

}
