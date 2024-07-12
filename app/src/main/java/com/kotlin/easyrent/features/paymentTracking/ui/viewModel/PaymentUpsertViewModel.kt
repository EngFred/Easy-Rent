package com.kotlin.easyrent.features.paymentTracking.ui.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.R
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.features.paymentTracking.domain.usecase.DeletePaymentUseCase
import com.kotlin.easyrent.features.paymentTracking.domain.usecase.GetPaymentByIdUseCase
import com.kotlin.easyrent.features.paymentTracking.domain.usecase.SavePaymentUseCase
import com.kotlin.easyrent.features.paymentTracking.ui.screens.upsert.PaymentUpsertUIEvents
import com.kotlin.easyrent.features.paymentTracking.ui.screens.upsert.PaymentUpsertUIState
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.GetAllRentalsUseCase
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.features.tenantManagement.domain.usecase.GetAllTenantsUseCase
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
class PaymentUpsertViewModel @Inject constructor(
    private val getAllRentalsUseCase: GetAllRentalsUseCase,
    private val getAllTenantsUseCase: GetAllTenantsUseCase,
    private val savePaymentUseCase: SavePaymentUseCase,
    private val deletePaymentUseCase: DeletePaymentUseCase,
    private val getPaymentByIdUseCase: GetPaymentByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUpsertUIState())
    val uiState = _uiState.asStateFlow()

    private val paymentId = savedStateHandle.get<String>(Keys.PAYMENT_ID)


    init {
        if ( paymentId != null ) {
            getPayment(paymentId)
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
        getRentals()
        getTenants()
    }

    fun onEvent(event: PaymentUpsertUIEvents) {
        if ( _uiState.value.fetchError != null || _uiState.value.upsertError != null ) {
            _uiState.update {
                it.copy(
                    fetchError = null,
                    upsertError = null
                )
            }
        }
        when(event) {
            PaymentUpsertUIEvents.DeletedPayment -> {

                if ( _uiState.value.payment != null && _uiState.value.selectedTenant != null) {
                    _uiState.update {
                        it.copy(
                            deletingPayment = true
                        )
                    }
                    deletePayment(_uiState.value.payment!!, _uiState.value.selectedTenant!!)
                }

            }
            is PaymentUpsertUIEvents.RentalSelected -> {

                if ( _uiState.value.selectedRental != event.rental ) {
                    _uiState.update {
                        it.copy(
                            selectedTenant = null,
                            lastAmountPaid = 0.0
                        )
                    }
                    _uiState.update {
                        it.copy(
                            selectedRental = event.rental,
                            rentalTenants = _uiState.value.allTenants.filter { it.rentalId == event.rental.id }
                        )
                    }

                    _uiState.update {
                        it.copy(
                            selectedTenant = if ( it.rentalTenants.size == 1 ) it.rentalTenants[0] else it.selectedTenant
                        )
                    }

                    if ( _uiState.value.rentalTenants.size == 1 ) {
                        _uiState.update {
                            it.copy(
                                lastAmountPaid = it.selectedRental!!.monthlyPayment.toDouble() - _uiState.value.selectedTenant!!.balance
                            )
                        }
                    }
                }

                _uiState.update {
                    it.copy(
                        showRentalsDropDownMenu = !it.showRentalsDropDownMenu
                    )
                }

                _uiState.value.selectedRental?.let {
                    validateAmount(_uiState.value.amount?.trim() ?: "0")
                }
            }
            PaymentUpsertUIEvents.SavedPayment -> {
                if ( _uiState.value.isFormValid ) {
                    savePayment(_uiState.value.selectedTenant!!)
                }
            }
            is PaymentUpsertUIEvents.TenantSelected -> {
                if ( _uiState.value.selectedTenant != event.tenant ) {
                    _uiState.update {
                        it.copy(
                            selectedTenant = event.tenant,
                            lastAmountPaid = it.selectedRental!!.monthlyPayment.toDouble() - event.tenant.balance,
                        )
                    }
                }

                _uiState.update {
                    it.copy(
                        showTenantsDropDownMenu =  !it.showTenantsDropDownMenu
                    )
                }
            }
            PaymentUpsertUIEvents.ToggledRentalsDropDownMenu -> {
                _uiState.update {
                    it.copy(
                        showRentalsDropDownMenu = !_uiState.value.showRentalsDropDownMenu,
                        showTenantsDropDownMenu = false
                    )
                }
            }
            PaymentUpsertUIEvents.ToggledTenantsDropDownMenu -> {
                _uiState.update {
                    it.copy(
                        showTenantsDropDownMenu = !_uiState.value.showTenantsDropDownMenu,
                        showRentalsDropDownMenu = false
                    )
                }
            }

            is PaymentUpsertUIEvents.AmountChanged -> {
                _uiState.update {
                    it.copy(
                        amount = event.amount
                    )
                }
                _uiState.value.selectedRental?.let {
                    validateAmount(event.amount.trim())
                }
            }
        }

        validateForm()
    }

    private fun getTenants() = viewModelScope.launch {
        getAllTenantsUseCase.invoke().collectLatest { result ->
            when(result) {
                is ServiceResponse.Error -> {
                    Log.d("TAG", "${result.message}")
                }
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    if ( result.data.isNotEmpty() && result.data.size == 1 ) {
                        _uiState.update {
                            it.copy(
                                selectedTenant = result.data[0]
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                allTenants = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getRentals() = viewModelScope.launch {
        getAllRentalsUseCase.invoke().collectLatest { result ->
            when(result) {
                is ServiceResponse.Error -> {
                    Log.d("TAG", "${result.message}")
                }
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    if ( result.data.isNotEmpty() && result.data.size == 1 ) {
                        _uiState.update {
                            it.copy(
                                selectedRental = result.data[0]
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                rentals = result.data,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getPayment( id: String ) = viewModelScope.launch(Dispatchers.IO) {
        val result = getPaymentByIdUseCase.invoke(id)
        Log.d("TAG", "Task done!")
        when(result) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        fetchError = result.message
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        payment = result.data,
                        paymentId = result.data?.id,
                        paymentDate = result.data?.date,
                        amount = result.data?.amount.toString(), //changes with user interaction
                        paymentCompleted = result.data?.completed ?: false
                    )
                }
            }
        }
    }


    private fun savePayment(tenant: Tenant) = viewModelScope.launch( Dispatchers.IO ) {
        val payment = Payment(
            id = _uiState.value.paymentId ?: UUID.randomUUID().toString(),
            rentalId = _uiState.value.selectedRental?.id!!,
            tenantId = _uiState.value.selectedTenant?.id!!,
            by = _uiState.value.selectedTenant!!.name,
            rentalName = _uiState.value.selectedRental!!.name,
            amount = _uiState.value.amount!!.toDouble(),
            date = _uiState.value.paymentDate ?: Date().time,
            completed = ((_uiState.value.amount!!.trim().toDouble() + _uiState.value.lastAmountPaid)) == _uiState.value.selectedRental!!.monthlyPayment.toDouble()
        )

        _uiState.update {
            it.copy(
                upserting = true
            )
        }

        val result = savePaymentUseCase.invoke(payment, tenant)
        Log.d("TAG", "Task done!")
        when(result){
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        upserting = false,
                        upsertError = result.message
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

    private fun deletePayment(payment: Payment, tenant: Tenant) = viewModelScope.launch(Dispatchers.IO) {
        val result = deletePaymentUseCase.invoke(payment, tenant)
        Log.d("TAG", "Task done!")
        when(result) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        deletingPayment = false,
                        deletingPaymentError = result.message
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        deletingPayment = false,
                        taskSuccessfull = true
                    )
                }
            }
        }
    }

    private fun validateForm(){
        val tenantValidationResults = _uiState.value.selectedTenant != null
        val rentalValidationResults = _uiState.value.selectedRental != null
        val amountValidationResults = !_uiState.value.amount.isNullOrEmpty() && _uiState.value.amountError == null
        val validationResults = (tenantValidationResults
                && rentalValidationResults
                && amountValidationResults) && !_uiState.value.paymentCompleted

        Log.wtf("TAG", "ValidationResult are $validationResults")

        _uiState.update {
            it.copy(
                isFormValid = validationResults
            )
        }
    }

    private fun validateAmount( amount: String ) {

        val amountError = when{
            amount.isEmpty() -> R.string.empty_payment
            amount.toDoubleOrNull() == null -> R.string.invalid_payment
            amount.toDoubleOrNull() != null -> {
                when{
                    (amount.toDouble() + _uiState.value.lastAmountPaid) > _uiState.value.selectedRental!!.monthlyPayment.toDouble() -> R.string.payment_greater_than_rent
                    else -> null
                }
            }

            else -> null
        }
        _uiState.update {
            it.copy(
                amountError = amountError
            )
        }
    }
}