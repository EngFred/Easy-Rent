package com.kotlin.easyrent.features.rentalManagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kotlin.easyrent.core.theme.myBackground
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.utils.Constants
import com.kotlin.easyrent.utils.getImageRequest

@Composable
fun RentalItem(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    onDelete: (Rental) -> Unit,
    rental: Rental,
    isDeleting: () -> Boolean,
    deletedRentalId: String,
) {

    val context = LocalContext.current
    Row(
        modifier = modifier
            .clickable { onClick(rental.id) }
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageReq = getImageRequest(rental.image?: Constants.DEFAULT_RENTAL_IMAGE, context)
        AsyncImage(
            model = imageReq,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .height(100.dp).width(180.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(myBackground)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column{
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = rental.name.replaceFirstChar { it.uppercase() },
                fontFamily = poppinsBold,
                fontSize = 20.sp
            )
            Text(
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = rental.location.replaceFirstChar { it.uppercase() },
                fontFamily = poppins,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                textAlign = TextAlign.End,
                text = if (rental.isSynced) "Synced" else "Not synced",
                fontFamily = poppinsBold,
                fontSize = 13.sp,
                color = if (rental.isSynced) myPrimary else MaterialTheme.colorScheme.error
            )
        }
    }
}