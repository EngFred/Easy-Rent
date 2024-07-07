package com.kotlin.easyrent.features.profile.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.usecses.GetLandlordUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidateNameUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidatePhoneNumberUseCase
import com.kotlin.easyrent.features.profile.domain.usecase.LogoutUseCase
import com.kotlin.easyrent.features.profile.domain.usecase.UpdateAddressUseCase
import com.kotlin.easyrent.features.profile.domain.usecase.UpdateBioUseCase
import com.kotlin.easyrent.features.profile.domain.usecase.UpdateDOBUseCase
import com.kotlin.easyrent.features.profile.domain.usecase.UpdateProfilePhotoUseCase
import com.kotlin.easyrent.features.profile.domain.usecase.UpdateTelUseCase
import com.kotlin.easyrent.features.profile.domain.usecase.UpdateUsernameUseCase
import com.kotlin.easyrent.features.profile.ui.screens.EditableInfo
import com.kotlin.easyrent.features.profile.ui.screens.ProfileUiEvents
import com.kotlin.easyrent.features.profile.ui.screens.ProfileUiState
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfilePhotoUseCase: UpdateProfilePhotoUseCase,
    private val updateBioUseCase: UpdateBioUseCase,
    private val updateTelUseCase: UpdateTelUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val updateDOBUseCase: UpdateDOBUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validateNameUseCase: ValidateNameUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val getLandlordUseCase: GetLandlordUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent( event: ProfileUiEvents) {
        resetState()
        when(event) {
            is ProfileUiEvents.ChangedInfoText -> {
                _uiState.update {
                    it.copy(
                        infoText = event.infoText
                    )
                }
            }
            is ProfileUiEvents.ChangedInfoToEdit -> {
                _uiState.update {
                    it.copy(
                        infoEdit = event.infoToEdit,
                        showDatePickerDialog = false,
                        showEditDialog = true,
                        infoText = event.infoText ?: "",
                        firstNameText = event.firstNameText ?: "",
                        lastNameText = event.lastNameText ?: ""
                    )
                }
            }
            ProfileUiEvents.LoggedOut -> {
                logOut()
            }
            is ProfileUiEvents.ProfileImageUrlChanged -> {
                _uiState.update {
                    it.copy(
                        profileImageUrl = event.imageUrl
                    )
                }
                updateProfilePhoto(_uiState.value.profileImageUrl)
            }

            ProfileUiEvents.EditClicked -> {
                when(_uiState.value.infoEdit) {
                    EditableInfo.Bio -> {
                        if ( _uiState.value.infoText.isNotEmpty() ) {
                            updateBio(_uiState.value.infoText)
                        }
                    }
                    EditableInfo.Tel -> {
                        val validationResult = validatePhoneNumberUseCase.execute(_uiState.value.infoText)
                        if ( validationResult == null ) {
                            updateTel(_uiState.value.infoText)
                        }else{
                            _uiState.update {
                                it.copy(
                                    serverError = validationResult
                                )
                            }
                        }
                    }
                    EditableInfo.Address -> {
                        if ( _uiState.value.infoText.isNotEmpty() ) {
                            updateAddress(_uiState.value.infoText)
                        }
                    }
                    EditableInfo.Name -> {
                        val firstNameValidationResult = validateNameUseCase.execute(_uiState.value.firstNameText)
                        val lastNameValidationResult = validateNameUseCase.execute(_uiState.value.lastNameText)

                        if ( firstNameValidationResult == null && lastNameValidationResult == null ) {
                            updateUserName(_uiState.value.lastNameText, _uiState.value.firstNameText)
                        } else{
                            _uiState.update {
                                it.copy(
                                    serverError = R.string.invalid_form
                                )
                            }
                        }
                    }

                }
            }

            ProfileUiEvents.ShowEditDialogToggled -> {
                _uiState.update {
                    it.copy(
                        showEditDialog = !it.showEditDialog,
                        showDatePickerDialog = false,
                        infoText = ""
                    )
                }
            }

            ProfileUiEvents.ShowDatePickerToggled -> {
                _uiState.update {
                    it.copy(
                        showDatePickerDialog = !it.showDatePickerDialog
                    )
                }
            }

            is ProfileUiEvents.SelectedDateOfBirth -> {
                _uiState.update {
                    it.copy(
                        selectedDateOfBirth = event.dob
                    )
                }
                updateDOB(_uiState.value.selectedDateOfBirth)
            }

            is ProfileUiEvents.ChangedFirstNameText -> {
                _uiState.update {
                    it.copy(
                        firstNameText = event.firstNameText
                    )
                }
            }
            is ProfileUiEvents.ChangedLastNameText -> {
                _uiState.update {
                    it.copy(
                        lastNameText = event.lastNameText
                    )
                }
            }

            ProfileUiEvents.Disposed -> {
                _uiState.update {
                    it.copy(
                        serverError = null,
                        showDatePickerDialog = false,
                        showEditDialog = false
                    )
                }
            }

            ProfileUiEvents.GetCurrentLandlordInfo -> {
                getLandlord()
            }
        }
    }

    private fun getLandlord() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                fetchingLandlordInfo = true
            )
        }
        getLandlordUseCase.invoke().collectLatest { res ->
            when(res) {
                is ServiceResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            fetchingLandlordError = res.message,
                            fetchingLandlordInfo = false
                        )
                    }
                }
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            fetchingLandlordInfo = false,
                            landlord = res.data
                        )
                    }
                }
            }
        }
    }


    private fun resetState() {
        if (_uiState.value.serverError != null) {
            _uiState.update {
                it.copy(
                    serverError = null
                )
            }
        }

        if (_uiState.value.editSuccessful) {
            _uiState.update {
                it.copy(
                    editSuccessful = false
                )
            }
        }
    }

    private fun logOut() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update {
            it.copy(
                loggingOut = true,
            )
        }

        val res  = logoutUseCase.invoke()

        Log.d("ProfileViewModel", "Task finished!")

        when(res) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        serverError = res.message,
                        loggingOut = false
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        loggedOut = true,
                    )
                }
            }
        }
    }

    private fun updateBio(bio: String) = viewModelScope.launch( Dispatchers.IO) {
        _uiState.update {
            it.copy(
                isEditing = true,
                showEditDialog = false,
                infoText = ""
            )
        }
        val response = updateBioUseCase.invoke(bio)

        Log.d("ProfileViewModel", "Task finished!")

        when(response) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        serverError = response.message,
                        isEditing = false
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        isEditing = false,
                        editSuccessful = true,
                    )
                }
            }
        }
    }

    private fun updateTel(tel: String) = viewModelScope.launch( Dispatchers.IO) {
        _uiState.update {
            it.copy(
                isEditing = true,
                showEditDialog = false,
                infoText = ""
            )
        }
        val response = updateTelUseCase(tel)

        Log.d("ProfileViewModel", "Task finished!")

        when(response) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        serverError = response.message,
                        isEditing = false
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        isEditing = false,
                        editSuccessful = true,
                    )
                }
            }
        }
    }

    private fun updateAddress(address: String) = viewModelScope.launch( Dispatchers.IO) {
        _uiState.update {
            it.copy(
                isEditing = true,
                showEditDialog = false,
                infoText = ""
            )
        }
        val response = updateAddressUseCase(address)

        Log.d("ProfileViewModel", "Task finished!")

        when(response) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        serverError = response.message,
                        isEditing = false
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        isEditing = false,
                        editSuccessful = true,
                    )
                }
            }
        }
    }

    private fun updateDOB(dob: Long) = viewModelScope.launch( Dispatchers.IO) {
        _uiState.update {
            it.copy(
                isEditing = true,
                showEditDialog = false,
                infoText = "",
                showDatePickerDialog = false
            )
        }
        val response = updateDOBUseCase(dob)

        Log.d("ProfileViewModel", "Task finished!")

        when(response) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        serverError = response.message,
                        isEditing = false
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        isEditing = false,
                        editSuccessful = true,
                    )
                }
            }
        }
    }

    private fun updateProfilePhoto(photoUrl: String) = viewModelScope.launch( Dispatchers.IO) {
        _uiState.update {
            it.copy(
                isUpdatingProfilePhoto = true, //for showing a progress indicator while updating a profile photo
                isEditing = true, // this helps to prevent any other user backend interactions while updating profile photo,
                showDatePickerDialog = false,
                showEditDialog = false
            )
        }
        val response = updateProfilePhotoUseCase(photoUrl)

        Log.d("ProfileViewModel", "Task finished!")

        when(response) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        serverError = response.message,
                        isUpdatingProfilePhoto = false,
                        isEditing = false
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        isUpdatingProfilePhoto = false,
                        editSuccessful = true,
                        isEditing = false
                    )
                }
            }
        }
    }

    private fun updateUserName(lastName: String, firstName: String) = viewModelScope.launch( Dispatchers.IO ) {
        _uiState.update {
            it.copy(
                isEditing = true,
                showEditDialog = false,
                infoText = "",
                showDatePickerDialog = false
            )
        }
        val response = updateUsernameUseCase.invoke(lastName.trim(), firstName.trim())

        Log.d("ProfileViewModel", "Task finished!")

        when(response) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        serverError = response.message,
                        isEditing = false
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        isEditing = false,
                        editSuccessful = true,
                    )
                }
            }
        }
    }
}