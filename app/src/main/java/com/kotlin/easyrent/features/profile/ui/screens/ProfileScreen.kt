package com.kotlin.easyrent.features.profile.ui.screens

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.hilt.navigation.compose.hiltViewModel
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.presentation.components.MyButton
import com.kotlin.easyrent.core.theme.myPrimary
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.profile.ui.components.AccountDetail
import com.kotlin.easyrent.features.profile.ui.components.ProfileImage
import com.kotlin.easyrent.features.profile.ui.viewModel.ProfileViewModel
import com.kotlin.easyrent.utils.formatDateToString
import com.kotlin.easyrent.utils.openGallery
import com.kotlin.easyrent.utils.showDatePickerDialog

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    val loggedInUser = sharedViewModel.loggedInUser.collectAsState().value

    LaunchedEffect(loggedInUser) {
        if ( loggedInUser == null ) { //in case of after login or signup
            Log.v("$", "Getting landlord's info....")
            viewModel.onEvent(ProfileUiEvents.GetCurrentLandlordInfo)
        }
    }

    LaunchedEffect(uiState.editSuccessful) {
        if( uiState.editSuccessful ) {
            Toast.makeText(context, R.string.info_edited_successfully, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(uiState.serverError) {
        if( uiState.serverError != null ) {
            Toast.makeText(context, uiState.serverError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(uiState.loggedOut) {
        if( uiState.loggedOut) {
            onLogout()
        }
    }

    DisposableEffect(true) {
        onDispose {
            viewModel.onEvent(ProfileUiEvents.Disposed)
        }
    }

    //launcher for getting image
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onEvent(ProfileUiEvents.ProfileImageUrlChanged(it.toString()))
        }
    }

    //launcher for requesting image permission
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openGallery(imagePickerLauncher)
        } else {
            // Permission not granted, handle accordingly
        }
    }


    val permissionStatus = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
        }
        else -> {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }


    if ( uiState.showEditDialog ) {
        if ( uiState.infoEdit != EditableInfo.Name ) {
            EditInfoDialog(
                onDismiss = {
                    viewModel.onEvent(ProfileUiEvents.ShowEditDialogToggled)
                },
                onConfirm = {
                    Toast.makeText(context, R.string.changing_info, Toast.LENGTH_LONG).show()
                    viewModel.onEvent(ProfileUiEvents.EditClicked)
                },
                infoToEdit = uiState.infoEdit,
                onValueChange = {
                    if( uiState.infoText.length < 300 ) {
                        viewModel.onEvent(ProfileUiEvents.ChangedInfoText(it))
                    }
                },
                infoText = { uiState.infoText }
            )
        } else {
            EditInfoDialog(
                onDismiss = {
                    viewModel.onEvent(ProfileUiEvents.ShowEditDialogToggled)
                },
                onConfirm = {
                    Toast.makeText(context, R.string.changing_info, Toast.LENGTH_LONG).show()
                    viewModel.onEvent(ProfileUiEvents.EditClicked)
                },
                infoToEdit = uiState.infoEdit,
                firstNameText = { uiState.firstNameText },
                lastNameText = { uiState.lastNameText },
                onLastNameChange = {
                    if( uiState.lastNameText.length < 20 ) {
                        viewModel.onEvent(ProfileUiEvents.ChangedLastNameText(it))
                    }
                },
                onFirstNameChange = {
                    if( uiState.firstNameText.length < 20 ) {
                        viewModel.onEvent(ProfileUiEvents.ChangedFirstNameText(it))
                    }
                }
            )
        }

    }

    if ( uiState.showDatePickerDialog ) {
        showDatePickerDialog(
            context,
            onSelectDOB = {
                viewModel.onEvent(ProfileUiEvents.SelectedDateOfBirth(it))
            }
        )
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when {
            uiState.fetchingLandlordInfo -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        color = myPrimary
                    )
                }
            }

            else -> {

                if (loggedInUser == null && uiState.landlord == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.user_not_found),
                            fontFamily = poppinsBold,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {

                    val landlord = loggedInUser ?: uiState.landlord

                    if ( uiState.landlord != null && loggedInUser == null ) {
                        sharedViewModel.setLoggedInUser(uiState.landlord)
                    }

                    if (landlord != null) {
                        ProfileImage(
                            imageUrl = if (landlord.profileImage.isNullOrEmpty()) uiState.profileImageUrl else landlord.profileImage,
                            onEdit = {
                                if (!uiState.loggingOut) {
                                    if (permissionStatus == PermissionChecker.PERMISSION_GRANTED) {
                                        openGallery(imagePickerLauncher)
                                    } else {
                                        // Request appropriate permission
                                        val permissionToRequest = when {
                                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                                                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                                            }

                                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                                                Manifest.permission.READ_MEDIA_IMAGES
                                            }

                                            else -> {
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                            }
                                        }
                                        launcher.launch(permissionToRequest)
                                    }
                                }
                            },
                            isUpdating = { uiState.isUpdatingProfilePhoto }
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    if (!uiState.isEditing && !uiState.loggingOut) {
                                        viewModel.onEvent(
                                            ProfileUiEvents.ChangedInfoToEdit(
                                                EditableInfo.Name,
                                                firstNameText = landlord.firstName,
                                                lastNameText = landlord.lastName
                                            )
                                        )
                                    }
                                }
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(
                                text = "${landlord.lastName} ${landlord.firstName}",
                                fontFamily = poppinsBold,
                                fontSize = 24.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    if (!uiState.isEditing && !uiState.loggingOut) {
                                        viewModel.onEvent(
                                            ProfileUiEvents.ChangedInfoToEdit(
                                                EditableInfo.Bio,
                                                landlord.about ?: ""
                                            )
                                        )
                                    }
                                }
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(
                                text = landlord.about
                                    ?: stringResource(id = R.string.say_something_about_yourself_message),
                                fontFamily = poppins,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                            IconButton(onClick = {
                                if (!uiState.isEditing && !uiState.loggingOut) {
                                    viewModel.onEvent(
                                        ProfileUiEvents.ChangedInfoToEdit(
                                            EditableInfo.Bio,
                                            landlord.about ?: ""
                                        )
                                    )
                                }
                            }) {
                                Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                            }
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.account_info_text),
                            fontFamily = poppinsBold,
                            color = Color.Gray,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.size(7.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                            ) {
                                AccountDetail(
                                    detail = "Email.",
                                    value = { landlord.email },
                                    onClick = {

                                    }
                                )
                                HorizontalDivider()
                                AccountDetail(
                                    detail = "Tel no.",
                                    value = { landlord.contactNumber },
                                    onClick = {
                                        if (!uiState.isEditing && !uiState.loggingOut) {
                                            viewModel.onEvent(
                                                ProfileUiEvents.ChangedInfoToEdit(
                                                    EditableInfo.Tel,
                                                    landlord.contactNumber
                                                )
                                            )
                                        }
                                    }
                                )
                                HorizontalDivider()
                                AccountDetail(
                                    detail = "Address.",
                                    value = {
                                        landlord.address
                                            ?: stringResource(id = R.string.not_provided)
                                    },
                                    onClick = {
                                        if (!uiState.isEditing && !uiState.loggingOut) {
                                            viewModel.onEvent(
                                                ProfileUiEvents.ChangedInfoToEdit(
                                                    EditableInfo.Address,
                                                    landlord.address ?: ""
                                                )
                                            )
                                        }
                                    }
                                )
                                HorizontalDivider()
                                AccountDetail(
                                    detail = "DOB.",
                                    value = {
                                        if (landlord.dateOfBirth != null) {
                                            formatDateToString(landlord.dateOfBirth)
                                        } else {
                                            stringResource(id = R.string.not_provided)
                                        }
                                    },
                                    onClick = {
                                        if (!uiState.isEditing && !uiState.loggingOut) {
                                            viewModel.onEvent(ProfileUiEvents.ShowDatePickerToggled)
                                        }
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        MyButton(
                            text = stringResource(id = R.string.log_out),
                            onClick = {
                                if (!uiState.loggingOut && !uiState.isEditing) {
                                    viewModel.onEvent(ProfileUiEvents.LoggedOut)
                                }
                            },
                            isLoading = { uiState.loggingOut },
                            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                            textColor = MaterialTheme.colorScheme.error
                        )
                    }

                }
            }
        }

    }
}



@Composable
fun EditInfoDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    infoToEdit: EditableInfo,
    onValueChange: ((String) -> Unit)? = null,
    onFirstNameChange: ((String) -> Unit)? = null,
    onLastNameChange: ((String) -> Unit)? = null,
    infoText: (() -> String)? = null,
    firstNameText: (() -> String)? = null,
    lastNameText: (() -> String)? = null,
) {

    val stringRes = when(infoToEdit) {
        EditableInfo.Bio -> {
            stringResource(id = R.string.edit_bio)
        }
        EditableInfo.Tel -> {
            stringResource(id = R.string.edit_contact_number)
        }
        EditableInfo.Address -> {
            stringResource(id = R.string.edit_address)
        }
        EditableInfo.Name -> {
            "Edit Name"
        }
    }

    val hintText = when(infoToEdit) {
        EditableInfo.Bio -> {
            "bio"
        }
        EditableInfo.Tel -> {
            stringResource(id = R.string.phone_number)
        }
        EditableInfo.Address -> {
            stringResource(id = R.string.address)
        }
        EditableInfo.Name -> {
            stringResource(id = R.string.name)
        }
    }

    val isEditingUsername = infoText?.invoke() == null  && firstNameText?.invoke() !=  null && lastNameText?.invoke() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringRes)
        },
        text = {
            if ( !isEditingUsername ) {
                TextField(
                    value = infoText!!.invoke(),
                    label = {
                        Text(text = hintText, fontFamily = poppins, fontWeight = FontWeight.Bold)
                    },
                    onValueChange = onValueChange!!,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = myPrimary,
                        unfocusedIndicatorColor = myPrimary,
                        cursorColor = myPrimary
                    )
                )
            } else {
                Column {
                    TextField(
                        value = lastNameText!!.invoke(),
                        onValueChange = onLastNameChange!!,
                        label = {
                            Text(text = stringResource(id = R.string.last_name), fontFamily = poppins, fontWeight = FontWeight.Bold)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = myPrimary,
                            unfocusedIndicatorColor = myPrimary,
                            cursorColor = myPrimary
                        )
                    )
                    TextField(
                        value = firstNameText!!.invoke(),
                        onValueChange = onFirstNameChange!!,
                        label = {
                            Text(text = stringResource(id = R.string.first_name), fontFamily = poppins, fontWeight = FontWeight.Bold)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = myPrimary,
                            unfocusedIndicatorColor = myPrimary,
                            cursorColor = myPrimary
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                }, enabled = if ( !isEditingUsername ) infoText!!.invoke().isNotEmpty() else (firstNameText!!.invoke().isNotEmpty() && lastNameText!!.invoke().isNotEmpty())
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

enum class EditableInfo {
    Name,
    Bio,
    Tel,
    Address
}

