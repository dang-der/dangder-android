package com.viewpoint.dangder.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.viewpoint.CreatePaymentForPassTicketMutation
import com.viewpoint.FetchLoginUserIsCertQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {

    suspend fun createPaymentForPassTicket(
        impUid: String,
        payMoney: Double
    ): ApolloResponse<CreatePaymentForPassTicketMutation.Data> {
        return withContext(Dispatchers.IO) {
            apolloClient.mutation(CreatePaymentForPassTicketMutation(impUid, payMoney)).execute()
        }
    }

    suspend fun fetchLoginUserIsCert() : ApolloResponse<FetchLoginUserIsCertQuery.Data>{
        return withContext(Dispatchers.IO){
            apolloClient.query(FetchLoginUserIsCertQuery()).execute()
        }
    }
}