package com.kotlin.easyrent.features.paymentTracking.domain.usecase

import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.features.paymentTracking.domain.repository.PaymentRepository
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import javax.inject.Inject

class SavePaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(payment: Payment, tenant: Tenant) = paymentRepository.savePayment(payment, tenant)
}