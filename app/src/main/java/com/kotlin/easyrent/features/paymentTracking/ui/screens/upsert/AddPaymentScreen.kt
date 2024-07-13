package com.kotlin.easyrent.features.paymentTracking.ui.screens.upsert

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kotlin.easyrent.core.presentation.components.MyButton
import com.kotlin.easyrent.core.presentation.components.MyTextField
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.paymentTracking.ui.viewModel.AddPaymentViewModel
import com.kotlin.easyrent.utils.formatCurrency

@Composable
fun AddPaymentScreen(
    modifier: Modifier,
    onTaskSuccess: () -> Unit,
    addPaymentViewModel: AddPaymentViewModel = hiltViewModel()
) {

    val uiState = addPaymentViewModel.uiState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(uiState.upsertError) {
        if ( uiState.upsertError != null ) {
            Toast.makeText(context, uiState.upsertError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(uiState.taskSuccessful) {
        if ( uiState.taskSuccessful ) {
            onTaskSuccess()
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState()),
    ) {


        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontFamily = poppinsBold)) {
                append("Last Amount Paid: ")
            }
            withStyle(
                style = SpanStyle(
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraBold
                )
            ) {
                append(formatCurrency(uiState.lastAmountPaid))
            }
        })

        Spacer(modifier = Modifier.size(10.dp))

        MyTextField(
            label = "New Amount paid",
            onTextChange = {
                addPaymentViewModel.onEvent(AddPaymentUiEvents.AmountChanged(it))
            },
            value = { uiState.amount ?: "" },
            isError = { uiState.amountError != null },
            errorMessage = {
                uiState.amountError
            },
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.size(14.dp))

        if (uiState.rentals.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You haven't created any rentals yet! Create atleast one to add a payment.",
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = myPrimary
                )
            }
        } else {

            Text(
                text = "Rental",
                fontFamily = poppins,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Button(
                    onClick = {
                        if (uiState.rentals.size == 1) {
                            Toast.makeText(
                                context,
                                "You have no other rentals created!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            addPaymentViewModel.onEvent(AddPaymentUiEvents.ToggledRentalsDropDownMenu)
                        }
                    },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text(
                        text = if (uiState.rentals.size == 1) "${uiState.selectedRental?.name}" else uiState.selectedRental?.name
                            ?: "Choose rental",
                        fontFamily = poppins,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                DropdownMenu(
                    expanded = uiState.showRentalsDropDownMenu,
                    onDismissRequest = {
                        addPaymentViewModel.onEvent(AddPaymentUiEvents.ToggledRentalsDropDownMenu)
                    }
                ) {
                    uiState.rentals.forEach { rental ->
                        DropdownMenuItem(
                            text = {
                                Text(text = rental.name, fontFamily = poppins)
                            },
                            onClick = {
                                addPaymentViewModel.onEvent(
                                    AddPaymentUiEvents.RentalSelected(
                                        rental
                                    )
                                )
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(27.dp))
            if (uiState.allTenants.isEmpty()) {
                Text(
                    text = "There are not tenants yet!",
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = myPrimary
                )
            } else {

                if (uiState.rentalTenants.isEmpty()) {
                    Text(
                        text = "The selected rental has no tenants!",
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = myPrimary
                    )
                } else {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.size(17.dp))
                    Text(
                        text = "Tenant",
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Column {
                        Button(
                            onClick = {
                                if (uiState.rentalTenants.size == 1) {
                                    Toast.makeText(
                                        context,
                                        "There are no more tenants inside this rental!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    if (uiState.selectedRental != null) {
                                        addPaymentViewModel.onEvent(AddPaymentUiEvents.ToggledTenantsDropDownMenu)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please select a rental first!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            },
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .height(55.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = if (uiState.rentalTenants.isNotEmpty() && uiState.selectedTenant != null) uiState.selectedTenant.name else "Choose tenant",
                                fontFamily = poppins,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        DropdownMenu(
                            expanded = uiState.showTenantsDropDownMenu,
                            onDismissRequest = {
                                addPaymentViewModel.onEvent(AddPaymentUiEvents.ToggledTenantsDropDownMenu)
                            }
                        ) {
                            uiState.rentalTenants.forEach { tenant ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = tenant.name, fontFamily = poppins)
                                    },
                                    onClick = {
                                        if (uiState.selectedRental != null) {
                                            addPaymentViewModel.onEvent(
                                                AddPaymentUiEvents.TenantSelected(
                                                    tenant
                                                )
                                            )
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Please select a rental first!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.size(16.dp))
        if (uiState.paymentCompleted) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppinsBold
                        )
                    ) {
                        append("Note: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppins
                        )
                    ) {
                        append("The selected tenant already completed the monthly rent!")
                    }
                },
                fontFamily = poppins,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        MyButton(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = "Add Payment",
            onClick = {
                if (!uiState.upserting) {
                    addPaymentViewModel.onEvent(AddPaymentUiEvents.SavedAddPayment)
                }
            },
            enabled = uiState.isFormValid,
            isLoading = { uiState.upserting }
        )
        Spacer(modifier = Modifier.size(12.dp))

    }
}