package com.kotlin.easyrent.features.paymentTracking.ui.screens.payments

import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant

sealed class PaymentsUiEvents {
    data object PaymentDeleted: PaymentsUiEvents()
    data object SelectedAllPayments: PaymentsUiEvents()
    data object DismissedDeleteDialog: PaymentsUiEvents()
    data class PaymentSelected(val payment: Payment) : PaymentsUiEvents()
    data class SelectedTenant(val tenant: Tenant) : PaymentsUiEvents()
}