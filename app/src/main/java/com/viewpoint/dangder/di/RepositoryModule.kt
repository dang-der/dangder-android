package com.viewpoint.dangder.di

import com.viewpoint.dangder.repository.AuthRepository
import com.viewpoint.dangder.repository.AuthRepositoryImpl
import com.viewpoint.dangder.repository.SettingsRepository
import com.viewpoint.dangder.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(authRepository: AuthRepositoryImpl) : AuthRepository

    @Binds
    abstract fun bindSettingsRepository(settingsRepository: SettingsRepositoryImpl) : SettingsRepository

}