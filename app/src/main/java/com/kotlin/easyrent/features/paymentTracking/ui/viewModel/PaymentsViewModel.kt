package com.kotlin.easyrent.features.paymentTracking.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment
import com.kotlin.easyrent.features.paymentTracking.domain.usecase.DeletePaymentUseCase
import com.kotlin.easyrent.features.paymentTracking.domain.usecase.GetAllPaymentsUseCase
import com.kotlin.easyrent.features.paymentTracking.ui.screens.payments.PaymentsUIState
import com.kotlin.easyrent.features.paymentTracking.ui.screens.payments.PaymentsUiEvents
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.features.tenantManagement.domain.usecase.GetAllTenantsUseCase
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val getAllPaymentsUseCase: GetAllPaymentsUseCase,
    private val deletePaymentUseCase: DeletePaymentUseCase,
    private val getAllTenantsUseCase: GetAllTenantsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentsUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getPayments()
        getTenants()
    }

    fun onEvent(event: PaymentsUiEvents){
        when(event){
            PaymentsUiEvents.DismissedDeleteDialog -> {
                _uiState.update {
                    it.copy(
                        showDeletePaymentDialog = false
                    )
                }
            }
            PaymentsUiEvents.PaymentDeleted -> {
                _uiState.update {
                    it.copy(
                        showDeletePaymentDialog = false
                    )
                }
                _uiState.value.payment?.let {
                    deletePayment(it)
                }
            }
            is PaymentsUiEvents.PaymentSelected -> {
                _uiState.update {
                    it.copy(
                        payment = event.payment,
                        showDeletePaymentDialog =  true,
                        deleteSuccessful = false
                    )
                }
            }

            is PaymentsUiEvents.SelectedTenant -> {
                if ( _uiState.value.selectedTenant != event.tenant ) {
                    _uiState.update {
                        it.copy(
                            selectedTenant = event.tenant
                        )
                    }
                    filterTenantsPayment(event.tenant)
                }
            }

            PaymentsUiEvents.SelectedAllPayments -> {
                if ( _uiState.value.selectedTenant != null) {
                    _uiState.update {
                        it.copy(
                            selectedTenant = null,
                            filteredPayments = it.allPayments
                        )
                    }
                }
            }
        }
    }

    private fun filterTenantsPayment(tenant: Tenant) {
        val filteredTenantPayments = _uiState.value.allPayments.filter { payment ->
            payment.tenantId == tenant.id
        }
        _uiState.update {
            it.copy(
                filteredPayments = filteredTenantPayments
            )
        }
    }

    private fun getPayments()  = viewModelScope.launch {
        getAllPaymentsUseCase().collect { result ->
            when(result) {
                is ServiceResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loadError = result.message
                        )
                    }
                }
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            allPayments = result.data
                        )
                    }
                }
            }
        }
    }

    private fun getTenants()  = viewModelScope.launch {
        getAllTenantsUseCase().collect { result ->
            when(result) {
                is ServiceResponse.Error -> Unit
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            tenants = result.data
                        )
                    }
                }
            }
        }
    }

    private fun deletePayment( payment: Payment ) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update {
            it.copy(
                deletingPayment = true,
                deleteError = null,
                showDeletePaymentDialog = false
            )
        }
        val result = deletePaymentUseCase.invoke(payment)
        Log.d("TAG", "Task done!")
        when (result) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        deletingPayment = false,
                        deleteError = result.message
                    )
                }
            }

            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        deletingPayment = false,
                        deleteSuccessful = true
                    )
                }
            }
        }
    }
}