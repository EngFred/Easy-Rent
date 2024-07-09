package com.kotlin.easyrent.features.tenantManagement.ui.screens.upsert

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental

sealed class UpsertTenantUiEvents {
    data class NameChanged(val name: String) : UpsertTenantUiEvents()
    data class AddressChanged(val address: String) : UpsertTenantUiEvents()
    data class PhoneChanged(val phone: String) : UpsertTenantUiEvents()
    data class EmailChanged(val email: String) : UpsertTenantUiEvents()
    data class RentalSelected(val rental: Rental) : UpsertTenantUiEvents()
    data class ImageUrlChanged(val imageUrl: String) : UpsertTenantUiEvents()
    data class DescriptionChanged(val description: String) : UpsertTenantUiEvents()
    data object AddedTenant : UpsertTenantUiEvents()
    data object DeletedTenant : UpsertTenantUiEvents()
    data object ToggledDropDownMenu : UpsertTenantUiEvents()

}