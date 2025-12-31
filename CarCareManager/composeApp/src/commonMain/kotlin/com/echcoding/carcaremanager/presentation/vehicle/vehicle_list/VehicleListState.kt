package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import com.echcoding.carcaremanager.domain.model.FuelType
import com.echcoding.carcaremanager.domain.model.OdometerUnit

/**
 * The UI in this project will use MVI architecture.
 * This class represents the Model or the entire state of the UI for the vehicle lis screen.
 * It bundles the state with all the actions that can be made in the View
 */
data class VehicleListState(
    val id: String? = null,
    val name: String = "",
    val maker: String = "",
    val model: String = "",
    val licensePlate: String = "",
    val year: String = "",
    val fuelType: FuelType = FuelType.GAS,
    val odometer: String = "",
    val odometerUnit: OdometerUnit = OdometerUnit.KILOMETERS,
    val isActive: Boolean = false
)
