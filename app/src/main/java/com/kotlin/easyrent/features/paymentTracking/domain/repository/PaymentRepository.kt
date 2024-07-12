package com.kotlin.easyrent.features.paymentTracking.domain.repository

import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun getAllPayments(): Flow<ServiceResponse<List<Payment>>>
    fun getPaymentsForRental( rentalId: String ) : Flow<ServiceResponse<List<Payment>>>
    fun getPaymentsForTenant( tenantId: String ) : Flow<ServiceResponse<List<Payment>>>
    suspend fun getUnsyncedPayments() : ServiceResponse<List<Payment>>
    suspend fun getDeletedPayments() : ServiceResponse<List<Payment>>
    suspend fun getPaymentById( paymentId: String ): ServiceResponse<Payment?>
    suspend fun deletePayment( payment: Payment, tenant: Tenant ): ServiceResponse<Unit>

    suspend fun savePayment(
        payment: Payment,
        tenant: Tenant
    ): ServiceResponse<Unit>
}