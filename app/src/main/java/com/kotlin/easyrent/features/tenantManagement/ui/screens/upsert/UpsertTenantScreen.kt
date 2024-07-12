package com.kotlin.easyrent.features.tenantManagement.ui.screens.upsert

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kotlin.easyrent.core.presentation.components.MyButton
import com.kotlin.easyrent.core.presentation.components.MyTextField
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.tenantManagement.ui.viewModel.UpsertTenantViewModel
import com.kotlin.easyrent.utils.calculateTimeInRental
import com.kotlin.easyrent.utils.formatCurrency

@Composable
fun UpsertTenantScreen(
    modifier: Modifier = Modifier,
    tenantId: String?,
    onTaskSuccess: () -> Unit,
    upsertTenantViewModel: UpsertTenantViewModel = hiltViewModel()
) {

    val uiState = upsertTenantViewModel.uiState.collectAsState().value
    val context = LocalContext.current
    LaunchedEffect(tenantId) {
        Log.d("TAG", "TenantId: $tenantId")
    }

    LaunchedEffect(uiState.upsertError) {
        if ( uiState.upsertError != null ) {
            Toast.makeText(context, uiState.upsertError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(uiState.deletingTenantError) {
        if ( uiState.deletingTenantError != null ) {
            Toast.makeText(context, uiState.deletingTenantError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(uiState.taskSuccessfull) {
        if ( uiState.taskSuccessfull ) {
            onTaskSuccess()
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            upsertTenantViewModel.resetErrorState()
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState()),
    ) {

        if( tenantId != null ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                uiState.moveInDate?.let {
                    val timeInRentals = calculateTimeInRental(it)
                    Column (
                        modifier = Modifier.padding(start = 12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Time in",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = timeInRentals,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                }
                Spacer(modifier = Modifier.size(13.dp))

                Column(
                    modifier = Modifier.padding(end = 12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Due Months",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = uiState.oldTenant?.unpaidMonths.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Spacer(modifier = Modifier.size(13.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.size(13.dp))
        }


        MyTextField(
            label = "Name",
            onTextChange = {
                upsertTenantViewModel.onEvent(UpsertTenantUiEvents.NameChanged(it))
            },
            value = { uiState.name ?: "" },
            isError = { uiState.nameError != null },
            errorMessage = {
                uiState.nameError
            }
        )
        MyTextField(
            label = "Email (optional)",
            onTextChange = {
                upsertTenantViewModel.onEvent(UpsertTenantUiEvents.EmailChanged(it))
            },
            value = { uiState.email ?: "" },
            isError = { uiState.emailError != null },
            errorMessage = {
                uiState.emailError
            },
            keyboardType = KeyboardType.Email
        )
        MyTextField(
            label = "Phone number",
            onTextChange = {
                upsertTenantViewModel.onEvent(UpsertTenantUiEvents.PhoneChanged(it))
            },
            value = { uiState.phone?: "" },
            isError = { uiState.phoneError != null },
            errorMessage = {
                uiState.phoneError
            },
            keyboardType = KeyboardType.Number
        )
        MyTextField(
            label = "Address (optional)",
            onTextChange = {
                upsertTenantViewModel.onEvent(UpsertTenantUiEvents.AddressChanged(it))
            },
            value = { uiState.address ?: "" }
        )
        MyTextField(
            label = "Description (optional) ",
            onTextChange = {
                upsertTenantViewModel.onEvent(UpsertTenantUiEvents.DescriptionChanged(it))
            },
            value = {uiState.description ?: ""}
        )

        Spacer(modifier = Modifier.size(14.dp))
        if ( uiState.rentals.isNotEmpty() ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column{
                    Button(
                        onClick = {
                            if ( uiState.rentals.size > 1 ) {
                                upsertTenantViewModel.onEvent(UpsertTenantUiEvents.ToggledDropDownMenu)
                            }
                        },
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .height(55.dp)
                            .width(200.dp)
                    ) {
                        Text(
                            text = uiState.selectedRental?.name ?: "Choose rental",
                            fontFamily = poppins,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    DropdownMenu(
                        expanded = uiState.showDropDownMenu,
                        onDismissRequest = {
                            upsertTenantViewModel.onEvent(UpsertTenantUiEvents.ToggledDropDownMenu)
                        }
                    ) {
                        uiState.rentals.forEach { rental ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = rental.name, fontFamily = poppins)
                                },
                                onClick = {
                                    upsertTenantViewModel.onEvent(UpsertTenantUiEvents.RentalSelected(rental))
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = if (tenantId == null) "Rent" else "Balance", fontFamily = poppins, color = Color.Gray)
                    Text(
                        text = if (uiState.balance != null) formatCurrency(uiState.balance.toDouble()) else "0.00",
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

        } else {
            Text(
                text = "It looks like you haven't created any rentals yet! Please create a rental first before adding a tenant.",
                fontFamily = poppins,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = myPrimary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyButton(
                modifier = Modifier.weight(1f),
                text = if (tenantId == null) "Add Tenant" else "Update Tenant",
                onClick = {
                    if ( !uiState.upserting && !uiState.deletingTenant ) {
                        upsertTenantViewModel.onEvent(UpsertTenantUiEvents.AddedTenant)
                    }
                },
                enabled = uiState.isFormValid,
                isLoading = { uiState.upserting }
            )
            if ( tenantId != null ) {
                Spacer(modifier = Modifier.width(14.dp))
                MyButton(
                    modifier = Modifier.weight(1f),
                    text = "Remove Tenant",
                    onClick = {
                        if ( !uiState.upserting && !uiState.deletingTenant ) {
                            upsertTenantViewModel.onEvent(UpsertTenantUiEvents.DeletedTenant)
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.error,
                    enabled = uiState.oldTenant != null,
                    isLoading = { uiState.deletingTenant }
                )
            }
        }
        Spacer(modifier = Modifier.size(12.dp))


        if ( uiState.selectedRental?.noOfRooms == 0 ) {
            Text(
                text = "All apartments for this rental are now occupied!",
                fontFamily = poppinsBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}