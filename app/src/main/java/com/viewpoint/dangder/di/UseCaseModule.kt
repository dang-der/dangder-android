package com.viewpoint.dangder.di

import com.viewpoint.dangder.domain.repository.AuthRepository
import com.viewpoint.dangder.domain.repository.ChatRepository
import com.viewpoint.dangder.domain.repository.DogRepository
import com.viewpoint.dangder.domain.repository.SettingsRepository
import com.viewpoint.dangder.domain.usecase.EnterChatRoomUseCase
import com.viewpoint.dangder.domain.usecase.auth.*
import com.viewpoint.dangder.domain.usecase.dog.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object UseCaseModule {

    // auth
    @Provides
    fun providesCheckLoggedInUseCase(authRepository: AuthRepository) =
        CheckLoggedInUseCase(authRepository)

    @Provides
    fun providedLoginUseCase(authRepository: AuthRepository) = LoginUseCase(authRepository)

    @Provides
    fun providesCreateEmailTokenUseCase(authRepository: AuthRepository) =
        CreateEmailTokenUseCase(authRepository)

    @Provides
    fun providesVerifyEmailTokenUseCase(authRepository: AuthRepository) =
        VerifyEmailTokenUseCase(authRepository)

    @Provides
    fun providesCreateUserUseCase(authRepository: AuthRepository) =
        CreateUserUseCase(authRepository)

    @Provides
    fun provideFetchUserUsecase(authRepository: AuthRepository) = FetchUserUseCase(authRepository)


    //dog
    @Provides
    fun providesCheckRegisteredDogUseCase(dogRepository: DogRepository) =
        CheckRegisteredDogUseCase(dogRepository)

    @Provides
    fun providesFetchCharactersUseCase(dogRepository: DogRepository) =
        FetchCharactersUseCase(dogRepository)

    @Provides
    fun providesFetchInterestsUseCase(dogRepository: DogRepository) =
        FetchInterestsUseCase(dogRepository)

    @Provides
    fun providesCreateDogUseCaseUseCase(dogRepository: DogRepository) =
        CreateDogUseCase(dogRepository)

    @Provides
    fun providesFetChAroundDogsUseCase(
        dogRepository: DogRepository,
        settingsRepository: SettingsRepository
    ) = FetchAroundDogsUseCase(dogRepository, settingsRepository)

    @Provides
    fun providesEnterChatRoomUseCase(
        chatRepository: ChatRepository,
        settingsRepository: SettingsRepository
    ) = EnterChatRoomUseCase(chatRepository, settingsRepository)
}