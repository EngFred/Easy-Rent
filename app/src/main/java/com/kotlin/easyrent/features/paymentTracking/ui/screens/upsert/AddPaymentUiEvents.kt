package com.kotlin.easyrent.features.paymentTracking.ui.screens.upsert

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant

sealed class AddPaymentUiEvents {
    data class TenantSelected(val tenant: Tenant) : AddPaymentUiEvents()
    data class RentalSelected(val rental: Rental) : AddPaymentUiEvents()
    data class AmountChanged(val amount: String) : AddPaymentUiEvents()
    data object SavedAddPayment : AddPaymentUiEvents()
    data object ToggledTenantsDropDownMenu : AddPaymentUiEvents()
    data object ToggledRentalsDropDownMenu : AddPaymentUiEvents()
}