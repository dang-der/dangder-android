package com.viewpoint.dangder.domain.usecase.payment

import com.viewpoint.dangder.domain.repository.PaymentRepository
import javax.inject.Inject

class BuyPassTicketUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {

    suspend operator fun invoke(impUid : String, payMoney : Double = 100.0): Boolean {
        try {
            val id = paymentRepository.createPaymentForPassTicket(impUid, payMoney)
            return id.isNotEmpty()
        }catch (e : Exception){
            throw e
        }
    }
}