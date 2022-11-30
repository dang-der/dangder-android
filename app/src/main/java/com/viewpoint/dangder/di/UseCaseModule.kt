package com.viewpoint.dangder.di

import com.viewpoint.dangder.repository.AuthRepository
import com.viewpoint.dangder.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object UseCaseModule {
    @Provides
    fun providesCheckLoggedInUseCase(authRepository: AuthRepository) = CheckLoggedInUseCase(authRepository)

    @Provides
    fun providedLoginUseCase(authRepository: AuthRepository) = LoginUseCase(authRepository)

    @Provides
    fun providesCreateEmailTokenUseCase(authRepository: AuthRepository) = CreateEmailTokenUseCase(authRepository)

    @Provides
    fun providesVerifyEmailTokenUseCase(authRepository: AuthRepository) = VerifyEmailTokenUseCase(authRepository)

    @Provides
    fun providesCreateUserUseCase(authRepository: AuthRepository) = CreateUserUseCase(authRepository)

    @Provides
    fun provideFetchUserUsecase(authRepository: AuthRepository) = FetchUserUseCase(authRepository)

}