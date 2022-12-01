package com.viewpoint.dangder.di

import com.viewpoint.dangder.repository.*
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

    @Binds
    abstract fun bindDogRepository(dogRepository: DogRepositoryImpl) : DogRepository

    @Binds
    abstract fun bindFileRepository(fileRepository: FileRepositoryImpl) : FileRepository

}