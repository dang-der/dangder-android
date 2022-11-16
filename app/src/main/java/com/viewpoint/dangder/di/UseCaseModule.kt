package com.viewpoint.dangder.di

import com.viewpoint.dangder.repository.AuthRepository
import com.viewpoint.dangder.usecase.CheckLoggedInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object UseCaseModule {
    @Provides
    fun providesCheckLoggedInUseCase(authRepository: AuthRepository) = CheckLoggedInUseCase(authRepository)
}