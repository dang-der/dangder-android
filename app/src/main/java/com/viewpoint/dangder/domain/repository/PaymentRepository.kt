package com.viewpoint.dangder.domain.repository

interface PaymentRepository {
    suspend fun createPaymentForPassTicket(impUid : String, payMoney : Double): String
    suspend fun fetchLoginUserIsCert() : Boolean
}