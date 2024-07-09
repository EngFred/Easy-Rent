package com.kotlin.easyrent.features.tenantManagement.ui.screens.tenants

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant

data class TenantsUiState(
    val isLoading: Boolean = true,
    @StringRes
    val error: Int? = null,
    val tenants: List<Tenant> = emptyList()
)
