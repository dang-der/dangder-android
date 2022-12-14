package com.viewpoint.dangder.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.viewpoint.*
import com.viewpoint.adapter.FetchSocialLoginUserQuery_ResponseAdapter
import com.viewpoint.type.CreateUserInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {

    suspend fun fetchLoginUser(): ApolloResponse<FetchLoginUserQuery.Data> {
        return withContext(Dispatchers.IO) {
            apolloClient.query(FetchLoginUserQuery()).execute()
        }
    }


    suspend fun fetchSocialLoginUser(): ApolloResponse<FetchSocialLoginUserQuery.Data> {
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchSocialLoginUserQuery()).execute()
        }
    }

    suspend fun userLogin(email : String, password : String): ApolloResponse<UserLoginMutation.Data> {
        return withContext(Dispatchers.IO){
            apolloClient.mutation(UserLoginMutation(email, password)).execute()
        }
    }

    suspend fun createMailToken(
        email: String,
        type: String
    ): ApolloResponse<CreateMailTokenMutation.Data> {
        return withContext(Dispatchers.IO) {
            apolloClient.mutation(CreateMailTokenMutation(email, type)).execute()
        }
    }

    suspend fun verifyMailToken(
        email: String,
        token: String
    ): ApolloResponse<VerifyMailTokenMutation.Data> {
        return withContext(Dispatchers.IO) {
            apolloClient.mutation(VerifyMailTokenMutation(email, token)).execute()
        }
    }

    suspend fun createUser(
        email: String,
        password: String,
        pet: Boolean
    ): ApolloResponse<CreateUserMutation.Data> {
        return withContext(Dispatchers.IO) {
            apolloClient.mutation(
                CreateUserMutation(
                    CreateUserInput(
                        email = email,
                        password = password,
                        pet = Optional.present(pet)
                    )
                )
            ).execute()
        }
    }

}