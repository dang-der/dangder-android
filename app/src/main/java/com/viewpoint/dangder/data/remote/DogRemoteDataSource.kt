package com.viewpoint.dangder.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.viewpoint.CreateDogMutation
import com.viewpoint.FetchCharactersQuery
import com.viewpoint.FetchInterestCategoryForProfileQuery
import com.viewpoint.GetDogInfoMutation
import com.viewpoint.type.CreateDogInput
import com.viewpoint.type.LocationInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DogRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {

    suspend fun getDogInfo(
        dogRegNum: String,
        ownerBirth: String
    ): ApolloResponse<GetDogInfoMutation.Data> {
        return withContext(Dispatchers.IO) {
            apolloClient.mutation(
                GetDogInfoMutation(
                    dogRegNum = dogRegNum,
                    ownerBirth = ownerBirth
                )
            ).execute()
        }
    }

    suspend fun createDog(
        createDogInput: CreateDogInput,
        dogRegNum: String,
        ownerBirth: String
    ): ApolloResponse<CreateDogMutation.Data> {
        return withContext(Dispatchers.IO){
            apolloClient.mutation(CreateDogMutation(
                createDogInput = createDogInput,
                dogRegNum = dogRegNum,
                ownerBirth = ownerBirth
            )).execute()
        }
    }

    suspend fun fetchCharacters() : ApolloResponse<FetchCharactersQuery.Data> {
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchCharactersQuery()).execute()
        }
    }

    suspend fun fetchInterests() : ApolloResponse<FetchInterestCategoryForProfileQuery.Data> {
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchInterestCategoryForProfileQuery()).execute()
        }
    }
}