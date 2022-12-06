package com.viewpoint.dangder.usecase.payment

import com.viewpoint.dangder.repository.PaymentRepository
import javax.inject.Inject

class CheckUserBuyPassTicketUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {

    suspend operator fun invoke(): Boolean {
        try {
            return paymentRepository.fetchLoginUserIsCert()
        }catch (e : Exception){
            throw e
        }
    }
}