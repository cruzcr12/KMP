package com.echcoding.carcaremanager.presentation.core.extensions

import com.echcoding.carcaremanager.domain.model.ControlType
import com.echcoding.carcaremanager.domain.model.Maintenance
import com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.components.MaintenanceStatus
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
fun Maintenance.calculateStatus(currentOdometer: Int): MaintenanceStatus {
    // Get the current date
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    // Calculate Date Threshold
    val daysSinceStart = initialDate.daysUntil(today)
    val isDateOverdue = dateInterval > 0 && daysSinceStart >= dateInterval  // Is overdue if it's past the due date
    val isDateDueSoon = dateInterval > 0 && (dateInterval - daysSinceStart) in 1..30 // It's due soon if it's within 30 days

    // Calculate Mileage Threshold
    val milesSinceStart = currentOdometer - initialOdometer
    val isMileageOverdue = odometerInterval > 0 && milesSinceStart >= odometerInterval // Is overdue if it has past the odometer interval
    val isMileageSoon = odometerInterval > 0 && (odometerInterval - milesSinceStart) in 1..500 // It's due soon if it's within 500 miles

    // Select the status based on the ControlType
    return when (controlType) {
        ControlType.MILEAGE -> {
            if (isMileageOverdue) MaintenanceStatus.Overdue
            else if (isMileageSoon) MaintenanceStatus.DueSoon
            else MaintenanceStatus.Upcoming
        }
        ControlType.TIME -> {
            if (isDateOverdue) MaintenanceStatus.Overdue
            else if (isDateDueSoon) MaintenanceStatus.DueSoon
            else MaintenanceStatus.Upcoming
        }
        ControlType.BOTH -> {
            // If either is overdue, it's overdue
            if (isMileageOverdue || isDateOverdue) MaintenanceStatus.Overdue
            // If either is soon, it's due soon
            else if (isMileageSoon || isDateDueSoon) MaintenanceStatus.DueSoon
            else MaintenanceStatus.Upcoming
        }
    }
}

/**
 * Generates a subtitle for the maintenance item based on the current odometer value.
 */
fun Maintenance.generateSubtitle(currentOdometer: Int): String {
    val milesRemaining = odometerInterval - (currentOdometer - initialOdometer)

    //TODO: use the corresponding mileage type according to the type from the current car
    return when {
        milesRemaining < 0 -> "Overdue by ${-milesRemaining} miles"
        milesRemaining > 0 -> "Due in $milesRemaining miles"
        else -> "Due now"
    }
}

