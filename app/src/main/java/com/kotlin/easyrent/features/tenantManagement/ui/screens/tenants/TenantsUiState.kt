package com.kotlin.easyrent.features.tenantManagement.ui.screens.tenants

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant

data class TenantsUiState(
    val isLoading: Boolean = true,
    @StringRes
    val error: Int? = null,
    val allTenants: List<Tenant> = emptyList(),
    val selectedRental: Rental? = null,
    val rentals: List<Rental> = emptyList(),
    val filteredTenants: List<Tenant> = emptyList()
)
