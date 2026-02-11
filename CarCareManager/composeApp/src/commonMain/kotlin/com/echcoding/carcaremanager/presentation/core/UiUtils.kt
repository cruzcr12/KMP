package com.echcoding.carcaremanager.presentation.core

import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Get the current year based on the date in the system's default timezone
 */
@OptIn(ExperimentalTime::class)
fun getCurrentYear(): Int {
    // Get the current date and time
    val today = getCurrentLocalDate()
    return today.year
}

@OptIn(ExperimentalTime::class)
fun getCurrentLocalDate(): LocalDate {
    // Get the system's default time zone
    val timeZone = currentSystemDefault()
    // Get today's date in the time zone
    val today = Clock.System.todayIn(timeZone)
    return today
}

@OptIn(ExperimentalTime::class)
fun convertMillisToLocalDate(milliseconds: Long): LocalDate {
    // Create an instant from epoch milliseconds
    val instant = Instant.fromEpochMilliseconds(milliseconds)

    // Convert the instant to a LocalDate using the UTC time zone
    return instant.toLocalDateTime(TimeZone.UTC).date
}