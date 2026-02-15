package com.echcoding.carcaremanager.app

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object CarAppGraph : Route

    @Serializable
    data object VehicleList : Route

    @Serializable
    data class VehicleDetails(val vehicleId: Int?) : Route

    @Serializable
    data object MaintenanceList : Route

    @Serializable
    data class MaintenanceDetails(val maintenanceId: Long?, val selectedVehicleId: Int, val selectedVehicleOdometer: Int) : Route

    @Serializable
    data class ExpenseDetails(val expenseId: Long?,
                              val selectedVehicleId: Int,
                              val selectedMaintenance: String,
                              val selectedVehicleOdometer: Int,
                              val selectedVehicleOdometerUnit: String): Route

}
