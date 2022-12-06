package com.viewpoint.dangder.repository

import com.viewpoint.dangder.data.remote.PaymentRemoteDataSource
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentRemoteDataSource: PaymentRemoteDataSource
) : PaymentRepository {
    override suspend fun createPaymentForPassTicket(impUid: String, payMoney: Double): String {
        val response = paymentRemoteDataSource.createPaymentForPassTicket(impUid, payMoney)

        if (response.hasErrors()) throw Exception(response.errors?.first()?.message)

        return response.data?.createPaymentForPassTicket?.id ?: throw Exception("데이터가 존재하지 않습니다.")
    }

    override suspend fun fetchLoginUserIsCert(): Boolean {
        val response = paymentRemoteDataSource.fetchLoginUserIsCert()

        if(response.hasErrors()) throw Exception(response.errors?.first()?.message)

        return  response.data?.fetchLoginUserIsCert ?: throw Exception("데이터가 존재하지 않습니다.")
    }


}