package com.kotlin.easyrent.features.expenseTracking.ui.screens.expenses

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense
import com.kotlin.easyrent.features.expenseTracking.ui.components.ExpensesList
import com.kotlin.easyrent.features.expenseTracking.ui.viewModel.ExpensesViewModel

@Composable
fun ExpensesScreen(
    modifier: Modifier = Modifier,
    onAddExpense: () -> Unit,
    expensesViewModel: ExpensesViewModel = hiltViewModel()
) {

    val uiState = expensesViewModel.uiState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(uiState.deleteError) {
        if ( uiState.deleteError != null ) {
            Toast.makeText(context, uiState.deleteError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(uiState.deleteSuccessful) {
        if ( uiState.deleteSuccessful) {
            Toast.makeText(context, "Expense deleted", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.deletingExpense) {
        if ( uiState.deletingExpense) {
            Toast.makeText(context, "Deleting expense...", Toast.LENGTH_LONG).show()
        }
    }

    if ( uiState.showDeleteExpenseDialog ) {
        ShowDeleteDialog(
            onDismiss = {
                 expensesViewModel.onEvent(ExpensesUiEvents.DismissedDeleteDialog)
            },
            onDelete = {
                expensesViewModel.onEvent(ExpensesUiEvents.ExpenseDeleted)
            },
            expense = uiState.expense
        )
    }

    when {
        uiState.isLoading || uiState.loadError != null  -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                if ( uiState.isLoading ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = myPrimary
                    )
                }
                if ( uiState.loadError != null ) {
                    Text(
                        text = stringResource(uiState.loadError),
                        fontFamily = poppins,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
        else -> {
            val expenses = uiState.expenses
            Scaffold(
                modifier = modifier,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            onAddExpense()
                        },
                        shape = CircleShape,
                        containerColor = myPrimary,
                        contentColor = Color.White
                    ) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = null )
                    }
                }
            ) { paddingValues ->
                Log.v("TAG", "$paddingValues")
                if (expenses.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "There are no expenses added yet! click on the button below to add an expense",
                            fontFamily = poppins,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                } else {
                    ExpensesList(
                        modifier = Modifier,
                        expenses = expenses,
                        onLongPress = {
                            if ( !uiState.deletingExpense ) {
                                expensesViewModel.onEvent(ExpensesUiEvents.ExpenseSelected(it))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ShowDeleteDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    expense: Expense?
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Confirm Action", fontFamily = poppinsBold)
        },
        text = {
            Column {
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("You are about to completely delete the ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppinsBold,
                        )
                    ) {
                        append(expense?.description)
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append(" expense ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("for ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppinsBold,
                        )
                    ) {
                        append(expense?.rentalName)
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append(" worth ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppinsBold,
                            color = myPrimary
                        )
                    ) {
                        append("UGX.${expense?.amount}")
                    }
                })
            }
        },
        confirmButton = {
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(7.dp)
            ) {
                Text(text = "Proceed", fontFamily = poppins, fontWeight = FontWeight.ExtraBold)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(7.dp)
            ) {
                Text(text = "Cancel", fontFamily = poppins, fontWeight = FontWeight.ExtraBold)
            }
        }
    )
}