package com.kotlin.easyrent.utils

import android.app.DatePickerDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.kotlin.easyrent.core.prefrences.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
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
    onSelectDOB: (Long) -> Unit
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
            }.timeInMillis
            onSelectDOB(selectedDate)
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

fun formatDateToString(dateInMills: Long) : String {
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dateInMills)
    return formattedDate
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

fun openGallery(launcher: ActivityResultLauncher<String>) {
    launcher.launch("image/*")
}

fun getTempImageUri(context: Context): Uri {
    val tmpFile = File.createTempFile(
        "tmp_image_file",
        ".jpg",
        context.getExternalFilesDir("EasyRent")
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        tmpFile
    )
}

suspend fun compressImage(context: Context, imageUri: Uri, maxWidth: Int, maxHeight: Int, quality: Int): Uri? {
    return withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(imageUri) ?: return@withContext null
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
        val targetWidth: Int
        val targetHeight: Int

        if (originalBitmap.width > originalBitmap.height) {
            targetWidth = maxWidth
            targetHeight = (maxWidth / aspectRatio).toInt()
        } else {
            targetHeight = maxHeight
            targetWidth = (maxHeight * aspectRatio).toInt()
        }

        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true)

        val outputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val compressedByteArray = outputStream.toByteArray()

        val compressedFile = File(context.cacheDir, "compressed_image.jpg")
        val fileOutputStream = FileOutputStream(compressedFile)
        fileOutputStream.write(compressedByteArray)
        fileOutputStream.flush()
        fileOutputStream.close()

        compressedFile.toUri()
    }
}

fun formatCurrency(amount: Double): String {
    try {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        val formattedAmount = numberFormat.format(amount)
        return "UGX.$formattedAmount"
    } catch (e: NumberFormatException) {
        return "UGX.$amount"
    }
}
fun formatCurrencyWithNoUGX(amount: Double): String {
    try {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        val formattedAmount = numberFormat.format(amount)
        return formattedAmount
    } catch (e: NumberFormatException) {
        return amount.toString()
    }
}

//fun calculateDaysInRental(moveInDate: Long): Long {
//    val currentDate = Date().time
//    val diffInMillis = currentDate - moveInDate
//    return TimeUnit.MILLISECONDS.toDays(diffInMillis)
//}

fun calculateTimeInRental(moveInDate: Long): String {
    val moveInLocalDate = Date(moveInDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val currentDate = LocalDate.now()

    val years = ChronoUnit.YEARS.between(moveInLocalDate, currentDate)
    val months = ChronoUnit.MONTHS.between(moveInLocalDate.plusYears(years), currentDate)
    val days = ChronoUnit.DAYS.between(moveInLocalDate.plusYears(years).plusMonths(months), currentDate)

    return when {
        years > 0 -> "$years years"
        months > 0 -> "$months months, $days days"
        else -> "$days days"
    }
}

fun getCurrentMonthDays(): Pair<String, Int> {
    val currentDate = LocalDate.now()
    val yearMonth = YearMonth.of(currentDate.year, currentDate.month)
    val daysInMonth = yearMonth.lengthOfMonth()
    val monthName = currentDate.month.name.lowercase()

    return Pair(monthName, daysInMonth)
}

fun getCurrentMonthAndYear(): Pair<String, Int> {
    val currentDate = LocalDate.now()
    val monthName = currentDate.month.name.lowercase()
    val year = currentDate.year

    return Pair(monthName, year)
}

fun getDayWithSuffix(day: Int): String {
    return when {
        day in 11..13 -> "${day}th"
        day % 10 == 1 -> "${day}st"
        day % 10 == 2 -> "${day}nd"
        day % 10 == 3 -> "${day}rd"
        else -> "${day}th"
    }
}


fun formatTimestampWithSuffix(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val zonedDateTime = instant.atZone(ZoneId.systemDefault())
    val dayWithSuffix = getDayWithSuffix(zonedDateTime.dayOfMonth)
    val month = zonedDateTime.month.name.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    val year = zonedDateTime.year
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mma")
    val time = zonedDateTime.format(timeFormatter).replace("AM", "am").replace("PM", "pm")

    return "$dayWithSuffix/$month/$year at $time"
}

fun formatDate(
    timestamp: Long,
    pattern: String
): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(date)
}