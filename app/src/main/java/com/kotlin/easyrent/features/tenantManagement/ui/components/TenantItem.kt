package com.kotlin.easyrent.features.tenantManagement.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant

@Composable
fun TenantItem(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    tenant: Tenant,
) {
    Column(
        modifier = modifier
            .clickable { onClick(tenant.id) }
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(7.dp))
        Text(text = tenant.name, fontFamily = poppinsBold, fontSize = 20.sp)
        Text(text = tenant.rentalName, fontFamily = poppins)
        Text(
            text = if (tenant.isSynced) "Synced" else "Not synced",
            fontFamily = poppins,
            fontSize = 12.sp,
            color = if (tenant.isSynced) myPrimary else MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(7.dp))
        HorizontalDivider()
    }
}