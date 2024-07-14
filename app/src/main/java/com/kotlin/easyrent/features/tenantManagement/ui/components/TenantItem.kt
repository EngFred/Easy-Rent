package com.kotlin.easyrent.features.tenantManagement.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.presentation.components.SyncIndicator
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.core.theme.teal
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.utils.formatCurrency
import com.kotlin.easyrent.utils.formatDate

@Composable
fun TenantItem(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    tenant: Tenant
) {
    
    Row(
        modifier = modifier
            .clickable { onClick(tenant.id) }
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painterResource(id = R.drawable.profile_picture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column{
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = tenant.name.replaceFirstChar { it.uppercase() },
                    fontFamily = poppinsBold,
                    fontSize = 20.sp
                )
                Text(
                    text = formatDate(pattern = "M/d/yy", timestamp = tenant.moveInDate),
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (tenant.balance == 0.0 ) "Completed" else formatCurrency(tenant.balance),
                    fontFamily = poppins,
                    modifier = Modifier.weight(1f).padding(end = 10.dp),
                    fontWeight = FontWeight.ExtraBold,
                    color = if (tenant.balance == 0.0 ) teal else Color.DarkGray
                )
                SyncIndicator(
                    isSynced = tenant.isSynced,
                    unpaidMonths = tenant.unpaidMonths
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = tenant.rentalName.replaceFirstChar { it.uppercase() },
                    fontFamily = poppins,
                    maxLines = 1,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
    HorizontalDivider()
}