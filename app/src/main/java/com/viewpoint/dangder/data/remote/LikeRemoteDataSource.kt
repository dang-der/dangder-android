package com.viewpoint.dangder.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.viewpoint.CreateDogMutation
import com.viewpoint.CreateLikeMutation
import com.viewpoint.IsLikeMutation
import com.viewpoint.type.CreateLikeInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LikeRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun isLike(sendDogId : String, receiveDogId : String) : ApolloResponse<IsLikeMutation.Data> {
        return withContext(Dispatchers.IO){
            apolloClient.mutation(IsLikeMutation(sendDogId, receiveDogId)).execute()
        }
    }

    suspend fun createLike(createLikeInput : CreateLikeInput) : ApolloResponse<CreateLikeMutation.Data>{
        return withContext(Dispatchers.IO){
            apolloClient.mutation(CreateLikeMutation(createLikeInput)).execute()
        }
    }
}