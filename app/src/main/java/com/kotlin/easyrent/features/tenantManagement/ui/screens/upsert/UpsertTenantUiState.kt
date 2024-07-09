package com.kotlin.easyrent.features.tenantManagement.ui.screens.upsert

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.features.tenantManagement.domain.modal.TenantStatus

data class UpsertTenantUiState(
    val tenantId: String? = null,
    val rentalId: String? = null,
    val isLoading: Boolean = true,
    val upserting: Boolean = false,
    val taskSuccessfull: Boolean = false,
    @StringRes
    val upsertError: Int? = null,
    @StringRes
    val fetchError: Int? = null,

    @StringRes
    val deletingTenantError: Int? = null,

    val deletingTenant: Boolean = false,

    val name: String? = "",
    @StringRes
    val nameError: Int? = null,
    val email: String? = "",
    @StringRes
    val emailError: Int? = null,
    val balance: String? = "0.00",
    @StringRes
    val balanceError: Int? = null,
    val phone: String? = null,
    @StringRes
    val phoneError: Int? = null,
    val rentalName: String? = null,
    val moveInDate: Long? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val address: String? = null,

    val isFormValid: Boolean = false,

    val rentals: List<Rental> = emptyList(),
    val selectedRental: Rental? = null,

    val showDropDownMenu: Boolean = false,

    val tenantStatus: TenantStatus = TenantStatus.New,
    val oldTenant: Tenant? = null,
    val madeChanges: Boolean = false,

    )
