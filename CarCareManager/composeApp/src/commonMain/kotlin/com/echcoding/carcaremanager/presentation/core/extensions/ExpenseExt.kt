package com.echcoding.carcaremanager.presentation.core.extensions

import com.echcoding.carcaremanager.domain.model.Expense

/**
 * Returns the odometer value formatted with thousands separators and its unit abbreviation.
 * Example: "45,230 mi" or "72,800 km"
 */
val Expense.formattedMileage: String
    get() = "${mileage.formatWithSeparators()} $mileageUnit"

/**
 * Returns the short abbreviation for the odometer unit.
 */
/*
val OdometerUnit.abbreviation: String
    get() = when (this) {
        OdometerUnit.MILES -> "mi"
        OdometerUnit.KILOMETERS -> "km"
    }
*/

