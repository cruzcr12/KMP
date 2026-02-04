package com.echcoding.carcaremanager.presentation.core


import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
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