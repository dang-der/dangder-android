package com.viewpoint.dangder.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.rx3.Rx3Apollo
import com.apollographql.apollo3.rx3.rxSingle
import com.viewpoint.CreateMailTokenMutation
import com.viewpoint.FetchLoginUserQuery
import com.viewpoint.UserLoginMutation
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {

    suspend fun fetchLoginUser(): ApolloResponse<FetchLoginUserQuery.Data> {
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchLoginUserQuery()).execute()
        }
    }

    suspend fun userLogin(email : String, password : String): ApolloResponse<UserLoginMutation.Data> {
        return withContext(Dispatchers.IO){
            apolloClient.mutation(UserLoginMutation(email, password)).execute()
        }
    }

    suspend fun createMailToken(email: String, type:String): ApolloResponse<CreateMailTokenMutation.Data> {
        return withContext(Dispatchers.IO){
            apolloClient.mutation(CreateMailTokenMutation(email, type)).execute()
        }
    }

}