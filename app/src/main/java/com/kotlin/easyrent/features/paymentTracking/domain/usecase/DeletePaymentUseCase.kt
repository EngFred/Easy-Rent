package com.kotlin.easyrent.features.paymentTracking.domain.usecase

import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.features.paymentTracking.domain.repository.PaymentRepository
import javax.inject.Inject

class DeletePaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(payment: Payment) = paymentRepository.deletePayment(payment)
}