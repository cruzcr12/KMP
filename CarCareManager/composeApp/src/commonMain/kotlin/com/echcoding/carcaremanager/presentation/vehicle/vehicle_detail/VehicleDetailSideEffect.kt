package com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail

sealed interface VehicleDetailSideEffect {
    data object NavigateBack: VehicleDetailSideEffect
    data class ShowToast(val message: String): VehicleDetailSideEffect
}