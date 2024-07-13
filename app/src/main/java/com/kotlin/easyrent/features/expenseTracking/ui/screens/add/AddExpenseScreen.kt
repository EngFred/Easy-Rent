package com.kotlin.easyrent.features.expenseTracking.ui.screens.add

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kotlin.easyrent.core.presentation.components.MyButton
import com.kotlin.easyrent.core.presentation.components.MyTextField
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.features.expenseTracking.ui.viewModel.AddExpensesViewModel

@Composable
fun AddExpenseScreen(
    modifier: Modifier = Modifier,
    onAddSuccessful: () -> Unit,
    addExpensesViewModel: AddExpensesViewModel = hiltViewModel()
) {

    val uiState = addExpensesViewModel.uiState.collectAsState().value
    val context =  LocalContext.current

    LaunchedEffect( uiState.addError ) {
        if (uiState.addError != null ) {
            Toast.makeText(context,  uiState.addError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect( uiState.addSuccessful ) {
        if (uiState.addSuccessful ) {
            onAddSuccessful()
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MyTextField(
            label = "Amount",
            onTextChange = {
                addExpensesViewModel.onEvent(AddExpenseUiEvents.AmountChanged(it))
            },
            value = { uiState.amount },
            isError = { uiState.amountError != null },
            errorMessage = { uiState.amountError },
            keyboardType = KeyboardType.Number
        )

        MyTextField(
            label = "Description",
            onTextChange = {
                addExpensesViewModel.onEvent(AddExpenseUiEvents.DescriptionChanged(it))
            },
            value = { uiState.description },
            isError = { uiState.descriptionError != null },
            errorMessage = { uiState.descriptionError }
        )

        Spacer(modifier = Modifier.size(20.dp))
        val buttonText = if(uiState.rentals.size == 1) {
            uiState.rentals[0].name
        } else {
            if ( uiState.selectedRental != null ) uiState.selectedRental.name else "Choose rental"
        }
        if ( uiState.rentals.isNotEmpty() ) {
            Column {
                MyButton(
                    modifier = Modifier.padding(horizontal = 45.dp),
                    text = buttonText,
                    onClick = {
                        if ( uiState.rentals.size != 1 ) {
                            addExpensesViewModel.onEvent(AddExpenseUiEvents.ChooseRentalClicked)
                        } else {
                            Toast.makeText(context, "You only have 1 rental!", Toast.LENGTH_LONG).show()
                        }
                    },
                    isLoading = { false }
                )
                DropdownMenu(
                    expanded = uiState.showRentalsDialog ,
                    onDismissRequest = {
                        addExpensesViewModel.onEvent(AddExpenseUiEvents.ChooseRentalClicked)
                    }
                ) {
                    uiState.rentals.forEach { rental ->
                        DropdownMenuItem(
                            text = { Text(
                                text = rental.name.replaceFirstChar { it.uppercaseChar() },
                                fontFamily = poppins,
                                fontWeight = FontWeight.ExtraBold
                            ) },
                            onClick = {
                                addExpensesViewModel.onEvent(AddExpenseUiEvents.RentalChanged(rental))
                            }
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Currently you don't have any rentals so you can't add an expense! Create a rental first",
                fontFamily = poppins,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.weight(1f))
        MyButton(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = "Save",
            onClick = {
                if ( !uiState.isAdding )
                 addExpensesViewModel.onEvent(AddExpenseUiEvents.SaveButtonClicked)
            },
            enabled = uiState.isFormValid,
            isLoading = { uiState.isAdding }
        )
    }
}