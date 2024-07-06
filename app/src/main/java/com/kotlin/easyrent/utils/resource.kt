package com.kotlin.easyrent.utils

import androidx.annotation.StringRes

sealed class ServiceResponse<out T> {
    data object Loading : ServiceResponse<Nothing>()
    data class Success<T>(val data: T) : ServiceResponse<T>()
    data class Error( @StringRes val message: Int ) : ServiceResponse<Nothing>()

}