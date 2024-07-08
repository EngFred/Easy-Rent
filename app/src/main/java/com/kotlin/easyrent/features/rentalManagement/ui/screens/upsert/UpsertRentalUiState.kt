package com.kotlin.easyrent.features.rentalManagement.ui.screens.upsert

import androidx.annotation.StringRes

data class UpsertRentalUiState(
    val rentalId: String? = null,
    val isLoading: Boolean = true,
    val upserting: Boolean = false,
    val upsertSuccessful: Boolean = false,
    @StringRes
    val upsertError: Int? = null,
    @StringRes
    val fetchError: Int? = null,

    val name: String? = "",
    @StringRes
    val nameError: Int? = null,
    val location: String? = "",
    @StringRes
    val locationError: Int? = null,
    val monthlyPayment: String? = null,
    @StringRes
    val monthlyPaymentError: Int? = null,
    val noOfRooms: String? = null,
    @StringRes
    val noOfRoomsError: Int? = null,
    val description: String? = null,
    val imageUrl: String? = null,

    val isFormValid: Boolean = false,

    val showPhotoOptionsDialog: Boolean = false
)
