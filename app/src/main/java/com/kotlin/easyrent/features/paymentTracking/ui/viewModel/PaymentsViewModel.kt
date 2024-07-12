package com.kotlin.easyrent.features.paymentTracking.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.features.paymentTracking.domain.usecase.GetAllPaymentsUseCase
import com.kotlin.easyrent.features.paymentTracking.ui.screens.payments.PaymentsUIState
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val getAllPaymentsUseCase: GetAllPaymentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentsUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getPayments()
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
                            payments = result.data
                        )
                    }
                }
            }
        }
    }
}