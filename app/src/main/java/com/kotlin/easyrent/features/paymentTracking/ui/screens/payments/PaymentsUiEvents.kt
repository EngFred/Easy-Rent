package com.kotlin.easyrent.features.paymentTracking.ui.screens.payments

import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment

sealed class PaymentsUiEvents {
    data object PaymentDeleted: PaymentsUiEvents()
    data object DismissedDeleteDialog: PaymentsUiEvents()
    data class PaymentSelected(val payment: Payment) : PaymentsUiEvents()
}