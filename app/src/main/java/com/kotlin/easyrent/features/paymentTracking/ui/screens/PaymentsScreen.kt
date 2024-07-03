package com.kotlin.easyrent.features.paymentTracking.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun PaymentsScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Payments",
            fontFamily = poppins,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}