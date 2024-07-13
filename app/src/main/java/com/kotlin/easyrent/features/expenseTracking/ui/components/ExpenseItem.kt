package com.kotlin.easyrent.features.expenseTracking.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense
import com.kotlin.easyrent.utils.formatCurrency

@Composable
fun ExpenseItem(
    modifier: Modifier = Modifier,
    onLongPress: (Expense) -> Unit,
    expense: Expense
) {

    Card(
        modifier = Modifier.padding(7.dp)
            .pointerInput(Unit) {
            detectTapGestures(
                onLongPress = { onLongPress(expense) }
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Text(text = expense.rentalName.replaceFirstChar { it.uppercase() }, fontFamily = poppinsBold)
            Text(text = expense.description.replaceFirstChar { it.uppercase() }, fontFamily = poppins)
            Text(text = formatCurrency(expense.amount), fontFamily = poppinsBold, color = myPrimary)
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = if (expense.isSynced) "Synced" else "Not synced",
                fontFamily = poppins,
                fontSize = 13.sp,
                color = if (expense.isSynced) myPrimary else MaterialTheme.colorScheme.error
            )
        }
    }
}