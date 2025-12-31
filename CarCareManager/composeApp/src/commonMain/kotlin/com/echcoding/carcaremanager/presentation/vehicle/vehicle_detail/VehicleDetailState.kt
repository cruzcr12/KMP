package com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail

import com.echcoding.carcaremanager.domain.model.FuelType
import com.echcoding.carcaremanager.domain.model.OdometerUnit
import com.echcoding.carcaremanager.domain.model.Vehicle

data class VehicleDetailState(
    val vehicle: Vehicle? = null,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false
)
