package com.kotlin.easyrent.features.paymentTracking.ui.screens.payments

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant

data class PaymentsUIState(
    val isLoading: Boolean = true,
    @StringRes
    val loadError: Int? = null,

    val allPayments: List<Payment> = emptyList(),
    val tenants: List<Tenant> = emptyList(),
    val filteredPayments: List<Payment> = emptyList(),
    val selectedTenant: Tenant? = null,
    val payment: Payment? = null,

    val showDeletePaymentDialog: Boolean = false,
    val deletingPayment: Boolean = false,
    @StringRes
    val deleteError: Int? = null,
    val deleteSuccessful: Boolean = false,
)
