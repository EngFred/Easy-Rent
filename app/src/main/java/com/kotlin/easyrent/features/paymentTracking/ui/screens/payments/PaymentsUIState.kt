package com.kotlin.easyrent.features.paymentTracking.ui.screens.payments

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment

data class PaymentsUIState(
    val isLoading: Boolean = true,
    @StringRes
    val loadError: Int? = null,

    val payments: List<Payment> = emptyList(),
    val payment: Payment? = null,

    val showDeletePaymentDialog: Boolean = false,
    val deletingPayment: Boolean = false,
    @StringRes
    val deleteError: Int? = null,
    val deleteSuccessful: Boolean = false,
)
