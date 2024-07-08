package com.kotlin.easyrent.features.rentalManagement.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental

@Composable
fun RentalsList(
    modifier: Modifier,
    rentals: List<Rental>,
    onClick: (String) -> Unit,
    onDelete: (Rental) -> Unit,
    isDeleting: () -> Boolean,
    deletedRentalId: String,
) {

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = rentals.size,
            key = { rentals[it].id }
        ) {
            RentalItem(
                rental = rentals[it],
                onClick = onClick,
                onDelete = onDelete,
                isDeleting = isDeleting,
                deletedRentalId = deletedRentalId
            )
        }
    }

}


