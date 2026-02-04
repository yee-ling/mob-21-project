package com.example.mob21project.ui.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
fun convertMinutesToTimeString(minutes: Long): String {
    val hour24 = (minutes / 60).toInt()
    val minute = (minutes % 60).toInt()

    val isPM = hour24 >= 12
    val hour12 = when {
        hour24 == 0 -> 12        // 12 AM
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }
    val amPm = if (isPM) "PM" else "AM"
    return String.format("%d:%02d %s", hour12, minute, amPm)
}
fun minutesToTimeString(minutes: Int): String {
    val hour = minutes / 60
    val min = minutes % 60

    val amPm = if (hour < 12) "AM" else "PM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return "%d:%02d %s".format(displayHour, min, amPm)
}
fun minutesToTimeStringTimeGrid(minutes: Int): String {
    val hour = minutes / 60
    val min = minutes % 60
    val time = LocalTime.of(hour, min)
    val formatter = DateTimeFormatter.ofPattern("hh:mm", Locale.ENGLISH)
    val formattedTime = time.format(formatter)
    return formattedTime
}
@OptIn(ExperimentalMaterial3Api::class)
fun convertTimeToMinutes(state: TimePickerState): Long {
    return (state.hour * 60 + state.minute).toLong()
}
fun startOfDay(millis: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}
