package com.kotlin.easyrent.features.paymentTracking.ui.screens.payments

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment

data class PaymentsUIState(
    val isLoading: Boolean = true,
    @StringRes
    val loadError: Int? = null,

    val payments: List<Payment> = emptyList()
)
