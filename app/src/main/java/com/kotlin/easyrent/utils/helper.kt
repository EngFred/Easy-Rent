package com.kotlin.easyrent.utils

import java.util.Calendar


fun getStartOfDay(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun getEndOfDay(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}

fun getStartOfWeek(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    return calendar.timeInMillis
}

fun getEndOfWeek(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    calendar.add(Calendar.WEEK_OF_YEAR, 1)
    return calendar.timeInMillis
}

fun getStartOfMonth(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.timeInMillis
}

fun getEndOfMonth(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    return calendar.timeInMillis
}

fun getStartOfYear(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_YEAR, 1)
    return calendar.timeInMillis
}

fun getEndOfYear(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR))
    return calendar.timeInMillis
}