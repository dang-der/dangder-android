package com.viewpoint.dangder.repository

import com.viewpoint.dangder.data.remote.AuthRemoteDataSource
import com.viewpoint.dangder.entity.User
import com.viewpoint.dangder.mapper.UserMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import timber.log.Timber
import java.util.PrimitiveIterator

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

        return UserMapper.mapToUser(userData)
    }

    override suspend fun fetchAutoLoginSetting(): Flow<Boolean?> {
        return settingsRepository.getIsLoginSetting()
    }

    override suspend fun fetchUserAccount(): Flow<List<String>> {
        return settingsRepository.getUserAccountSetting()
    }

    override suspend fun saveToken(token: String) {

    }

}
