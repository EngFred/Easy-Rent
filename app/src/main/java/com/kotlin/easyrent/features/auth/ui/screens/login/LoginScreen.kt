package com.kotlin.easyrent.features.auth.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onSignupClick: () -> Unit
) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Spacer(modifier = Modifier.size(60.dp))
        Button(onClick = {   onHomeClick() }) {
            Text(
                text = "Home",
                fontFamily = poppins,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(modifier = Modifier.size(26.dp))
        Text(
            text = "Signup",
            modifier = Modifier.clickable {
                onSignupClick()
            },
            fontFamily = poppins,
            fontWeight = FontWeight.ExtraBold
        )
    }
}