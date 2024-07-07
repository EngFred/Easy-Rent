package com.kotlin.easyrent.features.profile.ui.screens

sealed class ProfileUiEvents {

    data class ChangedInfoToEdit(
        val infoToEdit: EditableInfo,
        val infoText: String? = null,
        val lastNameText: String? = null,
        val firstNameText: String? = null
    ) : ProfileUiEvents()
    data class ChangedInfoText( val infoText: String ) : ProfileUiEvents()
    data class ChangedFirstNameText( val firstNameText: String ) : ProfileUiEvents()
    data class ChangedLastNameText( val lastNameText: String ) : ProfileUiEvents()
    data class ProfileImageUrlChanged( val imageUrl: String ) : ProfileUiEvents()
    data object LoggedOut : ProfileUiEvents()
    data object EditClicked : ProfileUiEvents()
    data object Disposed : ProfileUiEvents()
    data object ShowEditDialogToggled : ProfileUiEvents()
    data object GetCurrentLandlordInfo: ProfileUiEvents()

    data object ShowDatePickerToggled: ProfileUiEvents()
    data class SelectedDateOfBirth(val dob: Long) : ProfileUiEvents()
}