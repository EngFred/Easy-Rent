package com.kotlin.easyrent.features.tenantManagement.ui.screens.tenants

import android.util.Log
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
            val tenants = uiState.tenants
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
                if (tenants.isEmpty()) {
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
                    TenantsList(
                        modifier = Modifier,
                        tenants = tenants,
                        onClick = {
                            onUpdateTenant(it)
                        }
                    )
                }
            }
        }
    }
}