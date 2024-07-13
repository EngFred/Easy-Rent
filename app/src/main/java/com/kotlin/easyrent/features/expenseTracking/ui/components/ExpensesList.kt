package com.kotlin.easyrent.features.expenseTracking.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense

@Composable
fun ExpensesList(
    modifier: Modifier,
    expenses: List<Expense>,
    onLongPress: (Expense) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = expenses.size,
            key = { expenses[it].id }
        ) {
            ExpenseItem(onLongPress = onLongPress, expense = expenses[it])
        }
    }
}
