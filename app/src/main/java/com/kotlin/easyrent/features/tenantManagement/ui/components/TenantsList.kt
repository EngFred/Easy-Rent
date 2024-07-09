package com.kotlin.easyrent.features.tenantManagement.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant

@Composable
fun TenantsList(
    modifier: Modifier,
    tenants: List<Tenant>,
    onClick: (String) -> Unit,
) {

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = tenants.size,
            key = { tenants[it].id }
        ) {
            TenantItem(onClick = onClick, tenant = tenants[it] )
        }
    }

}
