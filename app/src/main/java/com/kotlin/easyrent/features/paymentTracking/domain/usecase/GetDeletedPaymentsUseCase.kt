package com.kotlin.easyrent.features.paymentTracking.domain.usecase

import com.kotlin.easyrent.features.paymentTracking.domain.repository.PaymentRepository
import javax.inject.Inject

class GetDeletedPaymentsUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke() = paymentRepository.getDeletedPayments()
}