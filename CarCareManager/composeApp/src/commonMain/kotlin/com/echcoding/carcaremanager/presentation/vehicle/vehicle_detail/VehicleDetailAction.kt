package com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail

import com.echcoding.carcaremanager.domain.model.Vehicle

sealed interface VehicleDetailAction {
    data object OnBackClick: VehicleDetailAction
    data object OnSaveVehicleClick: VehicleDetailAction
    data class OnStateChange(var vehicle: Vehicle?): VehicleDetailAction
    data class OnSelectedVehicleChange(val vehicle: Vehicle): VehicleDetailAction
}
