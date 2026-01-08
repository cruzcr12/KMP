package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

sealed interface VehicleListSideEffect {
    data class ShowSnackbar(val message: String): VehicleListSideEffect
}