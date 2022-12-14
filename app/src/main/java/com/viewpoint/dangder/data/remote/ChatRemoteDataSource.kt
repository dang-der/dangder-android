package com.viewpoint.dangder.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.viewpoint.FetchChatMessagesByChatRoomIdQuery
import com.viewpoint.FetchChatRoomQuery
import com.viewpoint.FetchChatRoomsQuery
import com.viewpoint.JoinChatRoomMutation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {

    suspend fun joinChatRoom(
        myDogId: String,
        pairDogId: String
    ): ApolloResponse<JoinChatRoomMutation.Data> {
        return withContext(Dispatchers.IO) {
            apolloClient.mutation(JoinChatRoomMutation(dogId = myDogId, chatPairId = pairDogId))
                .execute()
        }
    }

    suspend fun fetchChatRoom(roomId : String) : ApolloResponse<FetchChatRoomQuery.Data>{
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchChatRoomQuery(roomId)).execute()
        }
    }

    suspend fun fetchChatMessagesByChatRoomId(roomId: String) : ApolloResponse<FetchChatMessagesByChatRoomIdQuery.Data>{
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchChatMessagesByChatRoomIdQuery(roomId)).execute()
        }
    }

    suspend fun fetchChatRooms(dogId : String) : ApolloResponse<FetchChatRoomsQuery.Data>{
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchChatRoomsQuery(dogId)).execute()
        }
    }


}