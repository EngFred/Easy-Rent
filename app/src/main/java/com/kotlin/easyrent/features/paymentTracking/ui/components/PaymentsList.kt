package com.kotlin.easyrent.features.paymentTracking.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment

@Composable
fun PaymentsList(
    modifier: Modifier,
    payments: List<Payment>,
    onClick: (String) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = payments.size,
            key = { payments[it].id }
        ) {
            PaymentItem(onClick = onClick, payment = payments[it])
        }
    }
}
