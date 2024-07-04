package com.kotlin.easyrent.features.profile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.prefrences.Language
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel
) {

    val context = LocalContext.current
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedLanguage by rememberSaveable {
        mutableStateOf(Language.English.name)
    }


    if (showDialog){
        ShowAlertDialog(
            onDismiss = {
            showDialog = false
        }, onConfirm = {
                showDialog = false
                sharedViewModel.setLanguage(selectedLanguage, context)
            }
        )
    }


    val currentLanguage = sharedViewModel.currentLanguage.collectAsState(initial = Language.English.name.lowercase()).value

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(86.dp))
        Text(
            text = stringResource(R.string.profile),
            fontFamily = poppins,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Button(onClick = {
            expanded = !expanded
        }) {
            Text(text = stringResource(R.string.choose_language))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Language.entries.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.name) },
                    onClick = {
                        expanded = false
                        if (language.name.lowercase() != currentLanguage) {
                            showDialog = true
                            selectedLanguage = language.name
                        }
                    }
                )
            }
        }

    }
}

@Composable
fun ShowAlertDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.restart_app))
        },
        text = {
            Text(stringResource(R.string.restart_app_message))
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(stringResource(id = R.string.no))
            }
        }
    )
}
