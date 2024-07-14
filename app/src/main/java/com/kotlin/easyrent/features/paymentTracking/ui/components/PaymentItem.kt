package com.kotlin.easyrent.features.paymentTracking.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.core.theme.teal
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.utils.formatCurrency
import com.kotlin.easyrent.utils.formatDate

@Composable
fun PaymentItem(
    modifier: Modifier = Modifier,
    onLongPress: (Payment) -> Unit,
    payment: Payment,
) {
    Column(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress(payment) }
                )
            }
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(7.dp))
        CustomText(title = "Tenant", value = payment.by)
        CustomText(title = "Rental", value = payment.rentalName)
        CustomText(title = "Amount", value = formatCurrency(payment.amount))
        CustomText(title = "Date", value = formatDate(payment.date, "MMM dd, yyyy"))
        val paymentStatus = if( payment.completed ) "Completed" else "Pending"
        CustomText(
            title = "Status",
            value = paymentStatus,
            valueColor = if(payment.completed) teal else Color.DarkGray
        )
        Text(
            text = if (payment.isSynced) "Synced" else "Not synced",
            fontFamily = poppinsBold,
            fontSize = 14.sp,
            color = if (payment.isSynced) myPrimary else MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(7.dp))
        HorizontalDivider()
    }
}

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    valueColor: Color = Color.DarkGray
) {

    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = poppinsBold
                )
            ) {
                append("${title.replaceFirstChar { it.uppercase() }}: ")
            }
            withStyle(
                style = SpanStyle(
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraBold,
                    color = valueColor
                )
            ) {
                append(value)
            }
        },
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )

}