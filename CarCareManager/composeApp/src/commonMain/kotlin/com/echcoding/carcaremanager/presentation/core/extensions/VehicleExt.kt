package com.echcoding.carcaremanager.presentation.core.extensions

import com.echcoding.carcaremanager.domain.model.OdometerUnit
import com.echcoding.carcaremanager.domain.model.Vehicle

/**
 * Returns the odometer value formatted with thousands separators and its unit abbreviation.
 * Example: "45,230 mi" or "72,800 km"
 */
val Vehicle.formattedOdometer: String
    get() = "${odometer.formatWithSeparators()} ${odometerUnit.abbreviation}"

/**
 * Returns the short abbreviation for the odometer unit.
 */
val OdometerUnit.abbreviation: String
    get() = when (this) {
        OdometerUnit.MILES -> "mi"
        OdometerUnit.KILOMETERS -> "km"
    }

/**
 * Formats an integer with thousands separators (commas).
 */
fun Int.formatWithSeparators(): String {
    return this.toString()
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()
}
