package com.kotlin.easyrent.features.rentalManagement.ui.screens.rentals

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.features.rentalManagement.ui.components.RentalsList
import com.kotlin.easyrent.features.rentalManagement.ui.viewModel.RentalsViewModel

@Composable
fun RentalsScreen(
    modifier: Modifier = Modifier,
    onAddRental: () -> Unit,
    onUpdateRental: (String) -> Unit,
    rentalsViewModel: RentalsViewModel = hiltViewModel()
) {

    val uiState = rentalsViewModel.uiState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(uiState.rentalDeleteError) {
        if ( uiState.rentalDeleteError != null ) {
            Toast.makeText(context, uiState.rentalDeleteError, Toast.LENGTH_LONG).show()
        }
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
            val rentals = uiState.rentals
            Scaffold(
                modifier = modifier,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            if (!uiState.deletingRental){
                                onAddRental()
                            }
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
                if (rentals.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "There are no rentals added yet! click on the button below to add a rental",
                            fontFamily = poppins,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                } else {
                    RentalsList(
                        modifier = Modifier,
                        rentals = rentals,
                        onClick = {
                            if (!uiState.deletingRental) {
                                onUpdateRental(it)
                            }
                        },
                        onDelete = {
                            if (!uiState.deletingRental) {
                                rentalsViewModel.onEvent(RentalsUiEvents.RentalDeleted(it))
                            }
                        },
                        isDeleting = { uiState.deletingRental },
                        deletedRentalId = uiState.deletedRentalId
                    )
                }
            }
        }
    }
}


