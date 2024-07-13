package com.kotlin.easyrent.features.paymentTracking.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment

@Composable
fun PaymentsList(
    modifier: Modifier,
    payments: List<Payment>,
    onLongPress: (Payment) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = payments.size,
            key = { payments[it].id }
        ) {
            PaymentItem(onLongPress = onLongPress, payment = payments[it])
        }
    }
}
