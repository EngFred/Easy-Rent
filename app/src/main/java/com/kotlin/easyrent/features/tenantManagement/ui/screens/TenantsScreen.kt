package com.kotlin.easyrent.features.tenantManagement.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun TenantsScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.tenants),
            fontFamily = poppins,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}