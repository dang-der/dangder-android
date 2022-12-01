package com.viewpoint.dangder.di

import com.viewpoint.dangder.repository.AuthRepository
import com.viewpoint.dangder.repository.DogRepository
import com.viewpoint.dangder.repository.FileRepository
import com.viewpoint.dangder.usecase.auth.*
import com.viewpoint.dangder.usecase.dog.CheckRegisteredDogUseCase
import com.viewpoint.dangder.usecase.dog.CreateDogUseCase
import com.viewpoint.dangder.usecase.dog.FetchCharactersUseCase
import com.viewpoint.dangder.usecase.dog.FetchInterestsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object UseCaseModule {

    // auth
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


    //dog
    @Provides
    fun providesCheckRegisteredDogUseCase(dogRepository: DogRepository) = CheckRegisteredDogUseCase(dogRepository)

    @Provides
    fun providesFetchCharactersUseCase(dogRepository: DogRepository) = FetchCharactersUseCase(dogRepository)

    @Provides
    fun providesFetchInterestsUseCase(dogRepository: DogRepository) = FetchInterestsUseCase(dogRepository)

    @Provides
    fun providesCreateDogUseCaseUseCase(dogRepository: DogRepository, fileRepository: FileRepository) = CreateDogUseCase(dogRepository, fileRepository)

}