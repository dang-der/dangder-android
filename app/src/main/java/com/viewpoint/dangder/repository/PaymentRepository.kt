package com.viewpoint.dangder.repository

interface PaymentRepository {
    suspend fun createPaymentForPassTicket(impUid : String, payMoney : Double): String
    suspend fun fetchLoginUserIsCert() : Boolean
}