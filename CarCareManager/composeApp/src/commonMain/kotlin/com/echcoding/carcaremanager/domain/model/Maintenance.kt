package com.echcoding.carcaremanager.domain.model

import kotlinx.datetime.LocalDate

data class Maintenance(
    val id: Long?,
    val vehicleId: Int,
    val name: String,
    val description: String?,
    val initialOdometer: Int, // Initial value of the odometer to start controlling the maintenance
    val initialDate: LocalDate, // Initial date to start controlling the maintenance
    val odometerInterval: Int, // Interval in kilometers or miles
    val dateInterval: Int, // Interval in days
    val controlType: ControlType, // How to control when the maintenance is due
    val status: MaintenanceStatusType = MaintenanceStatusType.UPCOMING  // Current status of the maintenance
)

enum class ControlType { MILEAGE, TIME, BOTH }
enum class MaintenanceStatusType { OVERDUE, DUE_SOON, COMPLETED, UPCOMING }