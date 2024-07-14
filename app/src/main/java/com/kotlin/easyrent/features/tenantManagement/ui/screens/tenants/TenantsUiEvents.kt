package com.kotlin.easyrent.features.tenantManagement.ui.screens.tenants

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental

sealed class TenantsUiEvents {
    data object SelectedAllTenants: TenantsUiEvents()
    data class SelectedRental(val rental: Rental) : TenantsUiEvents()
}