package com.kotlin.easyrent.features.profile.ui.screens

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.auth.domain.modal.User
import com.kotlin.easyrent.utils.Constants

data class ProfileUiState(
    val infoEdit: EditableInfo = EditableInfo.Bio,
    val infoText: String = "",
    val firstNameText: String = "",
    val lastNameText: String = "",
    val profileImageUrl: String = Constants.DEFAULT_PROFILE_PICTURE,
    val isEditing: Boolean = false,
    @StringRes
    val serverError: Int? = null,
    val isUpdatingProfilePhoto: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDatePickerDialog: Boolean = false,
    val editSuccessful: Boolean = false,
    val isFormValid: Boolean = false,

    val loggedOut: Boolean = false,
    val loggingOut: Boolean = false,

    val selectedDateOfBirth: Long = 0,
    val landlord: User? = null,
    @StringRes
    val fetchingLandlordError: Int? = null,
    val fetchingLandlordInfo: Boolean = false
)
