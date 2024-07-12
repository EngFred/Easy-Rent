package com.kotlin.easyrent.features.paymentTracking.ui.screens.upsert

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.features.paymentTracking.domain.modal.PaymentStatus
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant

data class PaymentUpsertUIState(
    val paymentId: String? = null,
    val isLoading: Boolean = true,
    val upserting: Boolean = false,
    val taskSuccessfull: Boolean = false,
    @StringRes
    val upsertError: Int? = null,
    @StringRes
    val fetchError: Int? = null,

    @StringRes
    val deletingPaymentError: Int? = null,

    val deletingPayment: Boolean = false,

    val payment: Payment? = null,

    val amount: String? = null, //changes as a user types
    @StringRes
    val amountError: Int? = null,

    val paymentDate: Long? = null,
    val address: String? = null,

    val isFormValid: Boolean = false,

    val rentals: List<Rental> = emptyList(),
    val allTenants: List<Tenant> = emptyList(),
    val rentalTenants: List<Tenant> = emptyList(),
    val selectedRental: Rental? = null,
    val selectedTenant: Tenant? = null,

    val showRentalsDropDownMenu: Boolean = false,
    val showTenantsDropDownMenu: Boolean = false,

    val paymentStatus: PaymentStatus = PaymentStatus.New,
    val lastAmountPaid: Double = 0.0,
    val madeChanges: Boolean = false,
    val paymentCompleted: Boolean = false
)
