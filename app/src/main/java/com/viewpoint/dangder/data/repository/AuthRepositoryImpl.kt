package com.viewpoint.dangder.data.repository

import com.viewpoint.dangder.data.remote.AuthRemoteDataSource
import com.viewpoint.dangder.domain.repository.AuthRepository
import com.viewpoint.dangder.domain.repository.SettingsRepository
import com.viewpoint.dangder.domain.entity.User
import com.viewpoint.dangder.data.mapper.DogMapper
import com.viewpoint.dangder.data.mapper.UserMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val settingsRepository: SettingsRepository
) : AuthRepository {

    override suspend fun userLogin(email: String, password: String): String {
        val response = authRemoteDataSource.userLogin(email, password)

        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        return response.data?.userLogin ?: throw Exception("회원정보를 찾을 수 없습니다.")
    }

    override suspend fun fetchLoginUser(): User {
        val response = authRemoteDataSource.fetchLoginUser()
        if(response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val userData = response.data?.fetchLoginUser?.user ?: throw Exception("데이터가 없습니다.")
        val dogData = response.data?.fetchLoginUser?.dog ?: throw Exception("데이터가 없습니다.")

        val dog = DogMapper.mapToDogEntity(dogData)
        val user = UserMapper.mapToUser(userData)

        return user.copy(dog = dog)
    }

    override suspend fun fetchSocialLoginUser(): User {
        val response = authRemoteDataSource.fetchSocialLoginUser()
        if(response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val user = response.data?.fetchSocialLoginUser?: throw Exception("데이터가 없습니다.")
        return UserMapper.mapToUser(user)
    }


    override suspend fun fetchAutoLoginSetting(): Flow<Boolean?> {
        return settingsRepository.getAutoLoginSetting()
    }

    override suspend fun fetchUserAccount(): Flow<List<String>> {
        return settingsRepository.getUserAccount()
    }

    override suspend fun saveToken(token: String) {
        settingsRepository.saveAccessToken(token)
    }

    override suspend fun createEmailTokenForSignUp(email: String): Boolean {
        val response = authRemoteDataSource.createMailToken(email, "signUp")
        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        return response.data?.createMailToken ?: throw Exception("데이터가 없습니다.")
    }

    override suspend fun verifyEmailToken(email: String, token: String): Boolean {
        val response = authRemoteDataSource.verifyMailToken(email, token)
        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        return response.data?.verifyMailToken ?: throw Exception("데이터가 없습니다.")
    }

    override suspend fun createUser(email: String, password: String, pet: Boolean): User {
        val response = authRemoteDataSource.createUser(email, password, pet)
        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        val user = response.data?.createUser ?: throw Exception("데이터가 없습니다.")
        return UserMapper.mapToUser(user)
    }

}
