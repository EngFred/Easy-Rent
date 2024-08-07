package com.kotlin.easyrent.features.paymentTracking.domain.usecase

import com.kotlin.easyrent.features.paymentTracking.domain.repository.PaymentRepository
import javax.inject.Inject

class GetRentalPaymentsUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    operator fun invoke(rentalId: String) = paymentRepository.getPaymentsForRental(rentalId)
}