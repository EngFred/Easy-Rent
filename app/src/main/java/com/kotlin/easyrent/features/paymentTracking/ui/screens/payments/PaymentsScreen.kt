package com.kotlin.easyrent.features.paymentTracking.ui.screens.payments

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material3.OutlinedButton
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
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.features.paymentTracking.ui.components.PaymentsList
import com.kotlin.easyrent.features.paymentTracking.ui.viewModel.PaymentsViewModel
import com.kotlin.easyrent.utils.formatTimestampWithSuffix

@Composable
fun PaymentsScreen(
    modifier: Modifier = Modifier,
    onAddPayment: () -> Unit,
    paymentsViewModel: PaymentsViewModel = hiltViewModel()
) {

    val uiState = paymentsViewModel.uiState.collectAsState().value
    val context = LocalContext.current


    LaunchedEffect(uiState.deleteError) {
        if ( uiState.deleteError != null ) {
            Toast.makeText(context, uiState.deleteError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(uiState.deleteSuccessful) {
        if ( uiState.deleteSuccessful) {
            Toast.makeText(context, "Payment deleted", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.deletingPayment) {
        if ( uiState.deletingPayment) {
            Toast.makeText(context, "Deleting payment...", Toast.LENGTH_LONG).show()
        }
    }

    if ( uiState.showDeletePaymentDialog ) {
        ShowDeleteDialog(
            onDismiss = {
                paymentsViewModel.onEvent(PaymentsUiEvents.DismissedDeleteDialog)
            },
            onDelete = {
                paymentsViewModel.onEvent(PaymentsUiEvents.PaymentDeleted)
            },
            payment = uiState.payment
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
            val allPayments = uiState.allPayments
            Scaffold(
                modifier = modifier,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            onAddPayment()
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
                if (allPayments.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "There are no payments added yet! click on the button below to add a payment",
                            fontFamily = poppins,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                } else {
                    val allTenants = uiState.tenants //to sho tenants' names
                    val filteredPayments = uiState.filteredPayments

                    Column(
                        modifier =Modifier.fillMaxSize()
                    ) {
                        if ( allTenants.isNotEmpty() ) {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                item {
                                    Spacer(modifier = Modifier.size(10.dp))
                                    OutlinedButton(onClick = {
                                        paymentsViewModel.onEvent(PaymentsUiEvents.SelectedAllPayments)
                                    }, colors = ButtonDefaults.buttonColors(
                                        containerColor = if ( uiState.selectedTenant == null ) myPrimary else Color.Transparent,
                                        contentColor = if ( uiState.selectedTenant == null ) Color.White else myPrimary),
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = if (uiState.selectedTenant == null) myPrimary else Color.Gray
                                        )
                                    ) {
                                        Text(
                                            text = "All",
                                            fontFamily = poppins,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(10.dp))
                                }
                                items(allTenants.size) {
                                    OutlinedButton(onClick = {
                                        paymentsViewModel.onEvent(PaymentsUiEvents.SelectedTenant(allTenants[it]))
                                    }, colors = ButtonDefaults.buttonColors(
                                        containerColor = if ( uiState.selectedTenant == allTenants[it] ) myPrimary else Color.Transparent,
                                        contentColor = if ( uiState.selectedTenant == allTenants[it] ) Color.White else myPrimary),
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = if (uiState.selectedTenant == allTenants[it]) myPrimary else Color.Gray
                                        )
                                    ) {
                                        Text(
                                            text = allTenants[it].name.replaceFirstChar { it.uppercase() },
                                            fontFamily = poppins,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(10.dp))
                                }
                            }
                        }
                        if (uiState.filteredPayments.isEmpty() && uiState.selectedTenant != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "There are no payments for ${uiState.selectedTenant.name.replaceFirstChar { it.uppercase() }} found!",
                                    fontFamily = poppins,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }else {
                            PaymentsList(
                                modifier = Modifier,
                                payments = filteredPayments.ifEmpty { allPayments },
                                onLongPress = {
                                    if ( !uiState.deletingPayment ) {
                                        paymentsViewModel.onEvent(PaymentsUiEvents.PaymentSelected(it))
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowDeleteDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    payment: Payment?
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
                        append("You are about to completely delete the payment made by ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppinsBold,
                        )
                    ) {
                        append(payment?.by?.replaceFirstChar { it.uppercase() })
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append(" on ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppinsBold,
                        )
                    ) {
                        append(formatTimestampWithSuffix(payment?.date ?: 0))
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append(" for ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppinsBold,
                            color = myPrimary
                        )
                    ) {
                        append("UGX.${payment?.amount}")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append(" in ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppinsBold
                        )
                    ) {
                        append(payment?.rentalName?.replaceFirstChar { it.uppercase() })
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