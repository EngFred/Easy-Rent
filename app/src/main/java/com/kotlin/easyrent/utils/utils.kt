package com.kotlin.easyrent.utils

import android.app.DatePickerDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.kotlin.easyrent.core.prefrences.Language
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun setLocale(context: Context, language: Language) {
    try {
        val locale = when (language) {
            Language.Luganda -> Locale("lg")
            Language.Swahili -> Locale("sw")
            else -> Locale("en")
        }
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    } catch (e: Exception){
        Log.e("$", e.message.toString())
    }

}

fun restartApplication(context: Context) {
    val packageManager: PackageManager = context.packageManager
    val intent: Intent = packageManager.getLaunchIntentForPackage(context.packageName)!!
    val componentName: ComponentName = intent.component!!
    val restartIntent: Intent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(restartIntent)
    Runtime.getRuntime().exit(0)
}

fun showDatePickerDialog(
    context: Context,
    onSelectDOB: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val today = calendar.time
    calendar.add(Calendar.YEAR, -10)
    val minDate = calendar.time

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate)
            onSelectDOB(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.maxDate = today.time // Disable future dates
        datePicker.minDate = minDate.time // Set minimum date to 10 years ago
    }

    datePickerDialog.show()
}

fun getImageRequest( imageUrl: String, context: Context ): ImageRequest {
    val listener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, result: ErrorResult) {
            super.onError(request, result)
        }

        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
            super.onSuccess(request, result)
        }
    }
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .listener(listener)
        .crossfade(true)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(imageUrl)
        .diskCacheKey(imageUrl)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
}