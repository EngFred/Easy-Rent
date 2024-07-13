package com.kotlin.easyrent.features.rentalManagement.ui.screens.upsert

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraEnhance
import androidx.compose.material.icons.rounded.PhotoCameraBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import com.kotlin.easyrent.core.presentation.components.MyButton
import com.kotlin.easyrent.core.presentation.components.MyTextField
import com.kotlin.easyrent.core.theme.myBackground
import com.kotlin.easyrent.core.theme.poppins
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.rentalManagement.ui.viewModel.UpsertRentalViewModel
import com.kotlin.easyrent.utils.Constants
import com.kotlin.easyrent.utils.getTempImageUri
import com.kotlin.easyrent.utils.openGallery

@Composable
fun UpsertRentalsScreen(
    modifier: Modifier = Modifier,
    rentalId: String?,
    onTaskSuccess: () -> Unit,
    upsertRentalViewModel: UpsertRentalViewModel = hiltViewModel()
) {

    val uiState = upsertRentalViewModel.uiState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(rentalId) {
        Log.d("TAG", "RentalId is  $rentalId")
    }

    LaunchedEffect(uiState.upsertError) {
        if ( uiState.upsertError != null ) {
            Toast.makeText(context, uiState.upsertError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(uiState.deleteRentalError) {
        if ( uiState.deleteRentalError != null ) {
            Toast.makeText(context, uiState.deleteRentalError, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(uiState.taskSuccessfull) {
        if ( uiState.taskSuccessfull ) {
            onTaskSuccess()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            upsertRentalViewModel.resetErrorState()
        }
    }

    if (uiState.showConfirmDeleteDialog) {
        ShowConfirmDeleteDialog(
            onDismiss = {
                 upsertRentalViewModel.onEvent(UpsertRentalUiEvents.ShowConfirmDeleteDialogToggled)
            },
            onDelete = {
                upsertRentalViewModel.onEvent(UpsertRentalUiEvents.DeletedRental)
            },
            rental = uiState.oldRental
        )
    }

    //launcher for getting image
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            upsertRentalViewModel.onEvent(UpsertRentalUiEvents.ImageUrlChanged(it.toString()))
        }
    }

    // Launcher for taking a photo
    // Get temporary image URI
    val tempImageUri = remember { getTempImageUri(context) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            upsertRentalViewModel.onEvent(UpsertRentalUiEvents.ImageUrlChanged(tempImageUri.toString()))
        } else {
            Toast.makeText(context, "Failed to take photo", Toast.LENGTH_SHORT).show()
        }
    }

    //launcher for requesting image permission
    val photoGalleryPermLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openGallery(imagePickerLauncher)
        } else {
            // Permission not granted, handle accordingly
        }
    }

    //launcher for requesting camera permission
    val cameraPermLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            takePictureLauncher.launch(tempImageUri)
        } else {
            // Permission not granted, handle accordingly
        }
    }


    val photoGalleryPermStatus = when {
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

    val cameraPermissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

    if (uiState.showPhotoOptionsDialog) {
        ShowOptionsDialog(
            onDismiss = {
                 upsertRentalViewModel.onEvent(UpsertRentalUiEvents.ShowPhotoOptionsDialogToggled)
            },
            onTakePhoto = {
                upsertRentalViewModel.onEvent(UpsertRentalUiEvents.ShowPhotoOptionsDialogToggled)
                if ( cameraPermissionStatus == PermissionChecker.PERMISSION_GRANTED ) {
                    takePictureLauncher.launch(tempImageUri)
                } else {
                    cameraPermLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onChoosePhoto = {
                upsertRentalViewModel.onEvent(UpsertRentalUiEvents.ShowPhotoOptionsDialogToggled)
                if (photoGalleryPermStatus == PermissionChecker.PERMISSION_GRANTED) {
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
                    photoGalleryPermLauncher.launch(permissionToRequest)
                }
            }
        )
    }


    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(255.dp)
                .padding(horizontal = 7.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(myBackground),
        ) {
            AsyncImage(
                model = uiState.imageUrl ?: Constants.DEFAULT_RENTAL_IMAGE,
                contentDescription = null,
                imageLoader = ImageLoader(LocalContext.current),
                contentScale = ContentScale.Crop
            )
            IconButton(onClick = {
                if ( !uiState.upserting && !uiState.deletingRental ) {
                    upsertRentalViewModel.onEvent(UpsertRentalUiEvents.ShowPhotoOptionsDialogToggled)
                }
            }, modifier = Modifier
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(myBackground.copy(alpha = .5f))
            ) {
                Icon(
                    imageVector = Icons.Rounded.PhotoCameraBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            MyTextField(
                label = "Name",
                onTextChange = {
                    upsertRentalViewModel.onEvent(UpsertRentalUiEvents.NameChanged(it))
                },
                value = { uiState.name ?: "" },
                isError = { uiState.nameError != null },
                errorMessage = {
                    uiState.nameError
                }
            )
            MyTextField(
                label = "Location",
                onTextChange = {
                    upsertRentalViewModel.onEvent(UpsertRentalUiEvents.LocationChanged(it))
                },
                value = { uiState.location ?: "" },
                isError = { uiState.locationError != null },
                errorMessage = {
                    uiState.locationError
                }
            )
            MyTextField(
                label = "Monthly payment",
                onTextChange = {
                    upsertRentalViewModel.onEvent(UpsertRentalUiEvents.MonthlyPaymentChanged(it))
                },
                value = { uiState.monthlyPayment ?: "" },
                isError = { uiState.monthlyPaymentError != null },
                errorMessage = {
                    uiState.monthlyPaymentError
                },
                keyboardType = KeyboardType.Number
            )
            MyTextField(
                label = "No of rooms",
                onTextChange = {
                    upsertRentalViewModel.onEvent(UpsertRentalUiEvents.NumberOfRoomsChanged(it))
                },
                value = { uiState.noOfRooms ?: "" },
                isError = { uiState.noOfRoomsError != null },
                errorMessage = {
                    uiState.noOfRoomsError
                },
                keyboardType = KeyboardType.Number
            )
            MyTextField(
                label = "Description (optional) ",
                onTextChange = {
                    upsertRentalViewModel.onEvent(UpsertRentalUiEvents.DescriptionChanged(it))
                },
                value = { uiState.description ?: "" }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyButton(
                modifier = Modifier.weight(1f),
                text = if (rentalId == null) "Add Rental" else "Update Rental",
                onClick = {
                    if ( !uiState.upserting && !uiState.deletingRental ) {
                        upsertRentalViewModel.onEvent(UpsertRentalUiEvents.AddedRental)
                    }
                },
                enabled = uiState.isFormValid && !uiState.deletingRental,
                isLoading = { uiState.upserting }
            )
            if ( rentalId != null ) {
                Spacer(modifier = Modifier.width(14.dp))
                MyButton(
                    modifier = Modifier.weight(1f),
                    text = "Delete Rental",
                    onClick = {
                        if ( !uiState.upserting && !uiState.deletingRental ) {
                            upsertRentalViewModel.onEvent(UpsertRentalUiEvents.ShowConfirmDeleteDialogToggled)
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.error,
                    enabled = uiState.oldRental != null,
                    isLoading = { uiState.deletingRental }
                )
            }
        }
    }

}


@Composable
fun ShowOptionsDialog(
    onDismiss: () -> Unit,
    onTakePhoto: () -> Unit,
    onChoosePhoto: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Choose or Take Photo",
                fontFamily = poppinsBold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onTakePhoto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Rounded.CameraEnhance, contentDescription = null, tint = Color.White )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = "Camera", fontFamily = poppins)
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                Button(
                    onClick = onChoosePhoto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(7.dp)
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(imageVector = Icons.Rounded.PhotoCameraBack, contentDescription = null, tint = Color.White )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = "Gallery", fontFamily = poppins)
                    }
                }
            }
        },
        confirmButton = {

        }
    )
}

@Composable
fun ShowConfirmDeleteDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    rental: Rental?
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
                        append("You are about to completely the rental ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppinsBold,
                        )
                    ) {
                        append(rental?.name?.replaceFirstChar { it.uppercase() })
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("! Please note that this action can't be undone.")
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

