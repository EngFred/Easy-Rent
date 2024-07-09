package com.kotlin.easyrent.features.tenantManagement.ui.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.R
import com.kotlin.easyrent.features.auth.domain.usecase.isValidEmail
import com.kotlin.easyrent.features.auth.domain.usecase.isValidPhoneNumber
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.GetAllRentalsUseCase
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.features.tenantManagement.domain.modal.TenantStatus
import com.kotlin.easyrent.features.tenantManagement.domain.usecase.DeleteTenantUseCase
import com.kotlin.easyrent.features.tenantManagement.domain.usecase.GetTenantByIdUseCase
import com.kotlin.easyrent.features.tenantManagement.domain.usecase.UpsertTenantUseCase
import com.kotlin.easyrent.features.tenantManagement.ui.screens.upsert.UpsertTenantUiEvents
import com.kotlin.easyrent.features.tenantManagement.ui.screens.upsert.UpsertTenantUiState
import com.kotlin.easyrent.utils.Keys
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class UpsertTenantViewModel @Inject constructor(
    private val getAllRentalsUseCase: GetAllRentalsUseCase,
    private val getTenantByIdUseCase: GetTenantByIdUseCase,
    private val upsertTenantUseCase: UpsertTenantUseCase,
    private val deleteTenantUseCase: DeleteTenantUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpsertTenantUiState())
    val uiState = _uiState.asStateFlow()

    private val tenantId = savedStateHandle.get<String>(Keys.TENANT_ID)


    init {
        if ( tenantId != null ) {
            _uiState.update {
                it.copy(
                    tenantStatus = TenantStatus.Old
                )
            }
            getTenant(tenantId)
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
        getAllRentals()
    }

    fun onEvent(event: UpsertTenantUiEvents) {
        if( _uiState.value.upsertError !=  null || _uiState.value.fetchError != null || _uiState.value.deletingTenantError != null) {
            _uiState.update {
                it.copy(
                    upsertError = null,
                    fetchError = null,
                    deletingTenantError = null
                )
            }
        }
        when(event) {
            UpsertTenantUiEvents.AddedTenant -> {
                _uiState.value.selectedRental?.let {
                    upsertTenant(it, _uiState.value.tenantStatus)
                }
            }
            is UpsertTenantUiEvents.AddressChanged -> {
                _uiState.update {
                    it.copy(
                        address = event.address
                    )
                }
            }
            is UpsertTenantUiEvents.DescriptionChanged -> {
                _uiState.update {
                    it.copy(
                        description = event.description
                    )
                }
            }
            is UpsertTenantUiEvents.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = event.email
                    )
                }
                validateEmail(event.email)
            }
            is UpsertTenantUiEvents.ImageUrlChanged -> {
                _uiState.update {
                    it.copy(
                        imageUrl = event.imageUrl
                    )
                }
            }
            is UpsertTenantUiEvents.NameChanged -> {
                _uiState.update {
                    it.copy(
                        name = event.name
                    )
                }
                validateName(event.name)
            }
            is UpsertTenantUiEvents.PhoneChanged -> {
                _uiState.update {
                    it.copy(
                        phone = event.phone
                    )
                }
                validatePhone(event.phone)
            }
            is UpsertTenantUiEvents.RentalSelected -> {
                _uiState.update {
                    it.copy(
                        rentalName = event.rental.name,
                        rentalId = event.rental.id,
                        balance = event.rental.monthlyPayment.toString(),
                        selectedRental = event.rental,
                        showDropDownMenu = false
                    )
                }
            }

            UpsertTenantUiEvents.ToggledDropDownMenu -> {
                _uiState.update {
                    it.copy(
                        showDropDownMenu = !_uiState.value.showDropDownMenu
                    )
                }
            }

            UpsertTenantUiEvents.DeletedTenant -> {

                if ( _uiState.value.oldTenant !=  null ) {
                    _uiState.update {
                        it.copy(
                            deletingTenant = true
                        )
                    }
                    deleteTenant(_uiState.value.oldTenant!!, _uiState.value.selectedRental!! )
                }
            }
        }
        validateForm()
    }

    private fun upsertTenant(rental:  Rental, tenantStatus: TenantStatus) = viewModelScope.launch(Dispatchers.IO) {
        val tenant = Tenant(
            id = _uiState.value.tenantId ?: UUID.randomUUID().toString(),
            name = _uiState.value.name!!.trim(),
            email = _uiState.value.email?.trim(),
            phone = _uiState.value.phone!!,
            address = _uiState.value.address?.trim(),
            rentalName = _uiState.value.rentalName!!,
            moveInDate = _uiState.value.moveInDate ?: Date().time,
            rentalId = _uiState.value.rentalId!!,
            profilePhotoUrl = _uiState.value.imageUrl,
            description = _uiState.value.description?.trim(),
            balance = _uiState.value.balance!!.toDouble()
        )

        _uiState.update {
            it.copy(
                madeChanges = tenant != it.oldTenant?.copy(isSynced = false)
            )
        }

        Log.v("TAG", "MadeChanges: ${_uiState.value.madeChanges}")
        Log.v("TAG", "New Tenant: $tenant")
        Log.v("TAG", "Old Tenant: ${_uiState.value.oldTenant}")

        if ( _uiState.value.madeChanges ) {
            _uiState.update {
                it.copy(
                    upserting = true
                )
            }

            val res = upsertTenantUseCase.invoke(tenant, rental, tenantStatus)
            Log.v("TAG", "Task done!")
            when(res) {
                is ServiceResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            upserting = false,
                            upsertError = res.message
                        )
                    }
                }
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            upserting = false,
                            taskSuccessfull = true
                        )
                    }
                }
            }

        }else {
            Log.v("TAG", "No changes were made!")
            _uiState.update {
                it.copy(
                    taskSuccessfull = true
                )
            }
        }
    }

    private fun getTenant(tenantId: String) = viewModelScope.launch( Dispatchers.IO ) {
        val res = getTenantByIdUseCase.invoke(tenantId)
        Log.v("TAG", "Task done!")
        when(res) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        fetchError = res.message
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success ->{
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        tenantId = res.data?.id,
                        name = res.data?.name,
                        email = res.data?.email,
                        phone = res.data?.phone,
                        address = res.data?.address,
                        rentalName = res.data?.rentalName,
                        moveInDate = res.data?.moveInDate,
                        rentalId = res.data?.rentalId,
                        imageUrl = res.data?.profilePhotoUrl,
                        description = res.data?.description,
                        balance = res.data?.balance?.toString(),
                        oldTenant = res.data
                    )
                }
            }
        }
    }

    private fun getAllRentals() = viewModelScope.launch {
        getAllRentalsUseCase.invoke().collectLatest { res ->
            when(res) {
                is ServiceResponse.Error -> Unit
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    Log.v("TAG", "Received ${res.data.size} rentals")
                    if ( res.data.size == 1 ) {
                        _uiState.update {
                            it.copy(
                                rentalName = res.data[0].name,
                                rentalId = res.data[0].id,
                                balance = res.data[0].monthlyPayment.toString(),
                                rentals = res.data,
                                selectedRental = res.data[0]
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                rentals = res.data,
                                selectedRental = res.data.find { it.id == _uiState.value.rentalId }
                            )
                        }
                    }
                }
            }
        }
    }


    //validations
    private fun validateForm(){
        val nameValidationResults = !_uiState.value.name.isNullOrEmpty() && _uiState.value.name!!.length >= 3
        val rentalNameValidationResults = !_uiState.value.rentalName.isNullOrEmpty()
        val emailValidationResults = if ( _uiState.value.email.isNullOrEmpty() ) true else _uiState.value.emailError == null
        val phoneValidationResults = !_uiState.value.phone.isNullOrEmpty() && _uiState.value.phoneError == null
        val balanceValidationResults = !_uiState.value.balance.isNullOrEmpty()  && _uiState.value.balance != "0.00"
        val rooms = if ( _uiState.value.tenantStatus == TenantStatus.New ) _uiState.value.selectedRental?.noOfRooms != 0 else true
        val validationResults = nameValidationResults
                && rentalNameValidationResults
                && phoneValidationResults
                && balanceValidationResults
                && emailValidationResults
                && rooms

        Log.wtf("TAG", "ValidationResult are $validationResults")
        Log.wtf("TAG", "nameValidationResults: $nameValidationResults")
        Log.wtf("TAG", "emailValidationResults: $emailValidationResults")
        Log.wtf("TAG", "phoneValidationResul: $phoneValidationResults")
        Log.wtf("TAG", "balanceValidationResults: $balanceValidationResults")
        Log.wtf("TAG", "rooms: $rooms")

        _uiState.update {
            it.copy(
                isFormValid = validationResults
            )
        }
    }


    private fun  validateName( name: String ) {
        val nameError = when {
            name.isEmpty() -> R.string.empty_name
            name.length < 3 -> R.string.short_name
            else -> null
        }
        _uiState.update {
            it.copy(
                nameError =  nameError
            )
        }
    }

    private fun  validateEmail( email: String ) {
        val emailError = when {
            email.isEmpty() -> null
            !email.isValidEmail() -> R.string.invalid_email
            else -> null
        }
        _uiState.update {
            it.copy(
                emailError = emailError
            )
        }
    }

    private fun  validateBalance(balance: String) {
        val balanceError = when {
            balance.isEmpty() || balance == "0" -> R.string.empty_payment
            balance.toIntOrNull() == null -> R.string.invalid_payment
            else -> null
        }
        _uiState.update {
            it.copy(
                balanceError = balanceError
            )
        }
    }

    private fun  validatePhone(phone: String) {
        val phoneError = when {
            phone.isEmpty()  -> R.string.empty_phone_number
            !phone.isValidPhoneNumber() -> R.string.invalid_phone_number
            else -> null
        }
        _uiState.update {
            it.copy(
                phoneError = phoneError
            )
        }
    }

    fun resetErrorState() {
        _uiState.update {
            it.copy(
                fetchError = null,
                upsertError = null,
                deletingTenantError = null
            )
        }
    }


    private fun deleteTenant(tenant: Tenant, rental: Rental) = viewModelScope.launch {
        val res = deleteTenantUseCase.invoke( tenant, rental )
        Log.d("TAG", "Task done")
        when(res) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        deletingTenant = false,
                        deletingTenantError = res.message
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        taskSuccessfull = true
                    )
                }
            }
        }
    }
}