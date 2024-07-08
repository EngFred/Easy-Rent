package com.kotlin.easyrent.features.rentalManagement.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental

@Composable
fun RentalItem(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    onDelete: (Rental) -> Unit,
    rental: Rental,
    isDeleting: () -> Boolean,
    deletedRentalId: String,
) {
    Column(
        modifier = modifier
            .clickable { onClick(rental.id) }
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(7.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = rental.name, fontFamily = poppinsBold, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(10.dp))
            if ( isDeleting() && deletedRentalId == rental.id ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = myPrimary
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onDelete(rental)
                    }
                )
            }
        }
        Text(text = rental.location, fontFamily = poppins)
        Text(
            text = if (rental.isSynced) "Synced" else "Not synced",
            fontFamily = poppins,
            fontSize = 12.sp,
            color = if (rental.isSynced) myPrimary else MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(7.dp))
        HorizontalDivider()
    }
}