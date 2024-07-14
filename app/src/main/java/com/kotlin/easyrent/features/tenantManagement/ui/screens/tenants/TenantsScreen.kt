package com.kotlin.easyrent.features.tenantManagement.ui.screens.tenants

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.features.tenantManagement.ui.components.TenantsList
import com.kotlin.easyrent.features.tenantManagement.ui.viewModel.TenantsViewModel

@Composable
fun TenantsScreen(
    modifier: Modifier = Modifier,
    onAddTenant: () -> Unit,
    onUpdateTenant: (String) -> Unit,
    tenantsViewModel: TenantsViewModel = hiltViewModel(),
) {

    val uiState = tenantsViewModel.uiState.collectAsState().value
    val context = LocalContext.current


    when {
        uiState.isLoading || uiState.error != null  -> {
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
                if ( uiState.error != null ) {
                    Text(
                        text = stringResource(uiState.error),
                        fontFamily = poppins,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
        else -> {
            val allTenants = uiState.allTenants
            Scaffold(
                modifier = modifier,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            onAddTenant()
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
                if (allTenants.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "There are no tenants added yet! click on the button below to add a tenant",
                            fontFamily = poppins,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                } else {

                    //////
                    val rentals = uiState.rentals
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if ( rentals.isNotEmpty() && rentals.size > 1 ) {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                item {
                                    Spacer(modifier = Modifier.size(10.dp))
                                    OutlinedButton(onClick = {
                                        tenantsViewModel.onEvent(TenantsUiEvents.SelectedAllTenants)
                                    }, colors = ButtonDefaults.buttonColors(
                                        containerColor = if ( uiState.selectedRental == null ) myPrimary else Color.Transparent,
                                        contentColor = if ( uiState.selectedRental == null ) Color.White else myPrimary
                                    ),border = BorderStroke(
                                            width = 1.dp,
                                            color = if (uiState.selectedRental == null) myPrimary else Color.Gray
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
                                items(rentals.size) {
                                    OutlinedButton(onClick = {
                                        tenantsViewModel.onEvent(TenantsUiEvents.SelectedRental(rentals[it]))
                                    }, colors = ButtonDefaults.buttonColors(
                                        containerColor = if ( uiState.selectedRental == rentals[it] ) myPrimary else Color.Transparent,
                                        contentColor = if ( uiState.selectedRental == rentals[it] ) Color.White else myPrimary),
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = if (uiState.selectedRental == rentals[it]) myPrimary else Color.Gray
                                        )
                                    ) {
                                        Text(
                                            text = rentals[it].name.replaceFirstChar { it.uppercase() },
                                            fontFamily = poppins,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(10.dp))
                                }
                            }
                        }
                        if ( uiState.filteredTenants.isEmpty() && uiState.selectedRental != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "There are no tenants found in ${uiState.selectedRental.name}!",
                                    fontFamily = poppins,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        } else {
                            TenantsList(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                tenants = uiState.filteredTenants.ifEmpty { uiState.allTenants },
                                onClick = {
                                    onUpdateTenant(it)
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}