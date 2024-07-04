package com.kotlin.easyrent.features.auth.ui.screens.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun Signup(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.clickable {
                onHomeClick()
            },
            text = stringResource(id = R.string.home),
            fontFamily = poppins,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.size(26.dp))
        Text(
            text = stringResource(id = R.string.login),
            modifier = Modifier.clickable {
                onLoginClick()
            },
            fontFamily = poppins,
            fontWeight = FontWeight.ExtraBold
        )
    }
}