package com.kotlin.easyrent.features.paymentTracking.ui.components

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
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment

@Composable
fun PaymentItem(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    payment: Payment,
) {
    Column(
        modifier = modifier
            .clickable { onClick(payment.id) }
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(7.dp))
        Text(text = "Payment by ${payment.by}", fontFamily = poppins)
        Text(text = payment.rentalName, fontFamily = poppins)
        Text(text = payment.amount.toString(), fontFamily = poppinsBold, color = myPrimary)
        Text(
            text = if (payment.isSynced) "Synced" else "Not synced",
            fontFamily = poppins,
            fontSize = 12.sp,
            color = if (payment.isSynced) myPrimary else MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(7.dp))
        HorizontalDivider()
    }
}