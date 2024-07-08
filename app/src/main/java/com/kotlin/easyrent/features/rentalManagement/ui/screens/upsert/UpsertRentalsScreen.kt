package com.kotlin.easyrent.features.rentalManagement.ui.screens.upsert

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraEnhance
import androidx.compose.material.icons.rounded.PhotoCameraBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
import com.kotlin.easyrent.features.rentalManagement.ui.viewModel.UpsertRentalViewModel
import com.kotlin.easyrent.utils.getTempImageUri
import com.kotlin.easyrent.utils.openGallery

@Composable
fun UpsertRentalsScreen(
    modifier: Modifier = Modifier,
    rentalId: String?,
    onUpsertSuccess: () -> Unit,
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

    LaunchedEffect(uiState.upsertSuccessful) {
        if ( uiState.upsertSuccessful ) {
            onUpsertSuccess()
        }
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
                .height(280.dp)
                .padding(horizontal = 7.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(myBackground),
        ) {
            AsyncImage(
                model = uiState.imageUrl,
                contentDescription = null,
                imageLoader = ImageLoader(LocalContext.current),
                contentScale = ContentScale.Crop
            )
            IconButton(onClick = {
                if ( !uiState.upserting ) {
                    upsertRentalViewModel.onEvent(UpsertRentalUiEvents.ShowPhotoOptionsDialogToggled)
                }
            }, modifier = Modifier.align(Alignment.Center)
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
                value = {uiState.description ?: ""},
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.size(20.dp))
            MyButton(
                modifier = Modifier.padding(50.dp),
                text = if (rentalId == null) "Add Rental" else "Update Rental",
                onClick = {
                    if ( !uiState.upserting ) {
                        upsertRentalViewModel.onEvent(UpsertRentalUiEvents.AddedRental)
                    }
                },
                enabled = uiState.isFormValid,
                isLoading = { uiState.upserting }
            )
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

