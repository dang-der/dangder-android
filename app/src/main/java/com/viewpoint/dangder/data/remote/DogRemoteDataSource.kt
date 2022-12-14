package com.viewpoint.dangder.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.viewpoint.*
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

    suspend fun fetchAroundDog(dogId : String, page : Double) : ApolloResponse<FetchAroundDogsQuery.Data> {
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchAroundDogsQuery(dogId, page)).execute()
        }
    }

    suspend fun fetchDogsDistance(dogId: String):ApolloResponse<FetchDogsDistanceQuery.Data>{
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchDogsDistanceQuery(dogId)).execute()
        }
    }

    suspend fun fetchOneDog(dogId: String) : ApolloResponse<FetchOneDogQuery.Data>{
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchOneDogQuery(dogId)).execute()
        }
    }
}