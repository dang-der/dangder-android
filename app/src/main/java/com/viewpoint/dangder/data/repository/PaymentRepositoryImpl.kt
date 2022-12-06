package com.viewpoint.dangder.data.repository

import com.viewpoint.dangder.data.remote.PaymentRemoteDataSource
import com.viewpoint.dangder.domain.repository.PaymentRepository
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