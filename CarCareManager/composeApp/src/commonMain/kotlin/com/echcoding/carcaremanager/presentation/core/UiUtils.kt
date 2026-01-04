package com.echcoding.carcaremanager.presentation.core

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
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
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    return today.year
}