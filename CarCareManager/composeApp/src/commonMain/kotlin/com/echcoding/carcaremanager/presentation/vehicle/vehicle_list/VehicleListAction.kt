package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import com.echcoding.carcaremanager.domain.model.Vehicle

/***
 * This interface represents the intent in the MVI architecture.
 * The intent represents an user action or event that triggers a state change
 */
sealed interface VehicleListAction {
    data class OnVehicleSelected(val vehicle: Vehicle) : VehicleListAction
    data object OnAddVehicleClick : VehicleListAction
    data class OnEditVehicle(val vehicle: Vehicle) : VehicleListAction
    data class OnDeleteVehicle(val vehicle: Vehicle) : VehicleListAction
    data class OnTabSelected(val index: Int) : VehicleListAction
}