package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import com.echcoding.carcaremanager.domain.model.FuelType
import com.echcoding.carcaremanager.domain.model.OdometerUnit
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.UiText

/**
 * The UI in this project will use MVI architecture.
 * This class represents the Model or the entire state of the UI for the vehicle lis screen.
 * It bundles the state with all the actions that can be made in the View
 */
data class VehicleListState(
    val selectedVehicle: Vehicle? = null,
    val vehicles: List<Vehicle> = emptyList(),
    val isActive: Boolean = false,
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false, // determine if the list of vehicles is being fetched
    val errorMessage: UiText? = null, // to display any error when loading the list of vehicles
)
