package com.kotlin.easyrent.features.paymentTracking.ui.screens.upsert

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant

sealed class PaymentUpsertUIEvents {
    data class TenantSelected(val tenant: Tenant) : PaymentUpsertUIEvents()
    data class RentalSelected(val rental: Rental) : PaymentUpsertUIEvents()
    data class AmountChanged(val amount: String) : PaymentUpsertUIEvents()
    data object SavedPayment : PaymentUpsertUIEvents()
    data object DeletedPayment : PaymentUpsertUIEvents()
    data object ToggledTenantsDropDownMenu : PaymentUpsertUIEvents()
    data object ToggledRentalsDropDownMenu : PaymentUpsertUIEvents()
}